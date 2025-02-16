use std::env;
use std::fmt::Debug;
use std::os::windows::process::CommandExt;
use std::path::Path;
use std::process::{Command, Stdio};
use std::time::Duration;
use winapi::um::winbase::CREATE_NO_WINDOW;

pub fn get_current_executable_path() -> Option<String> {
    let path = match env::current_exe() {
        Ok(path) => path,
        Err(_) => return None,
    };

    let parent = match path.parent() {
        Some(parent) => parent,
        None => return None,
    };

    let path_str = match parent.to_str() {
        Some(path_str) => path_str,
        None => return None,
    };

    Some(path_str.to_string())
}

fn get_java_executable_path() -> Option<String> {
    let executable_path = get_current_executable_path();

    if executable_path.is_none() {
        return None;
    }

    let executable_path = executable_path.unwrap();
    let installation_path = Path::new(executable_path.as_str());

    let java_executable_path = if cfg!(target_os = "windows") {
        Path::new("runtime\\bin\\java.exe")
    } else {
        Path::new("runtime/bin/java")
    };

    let java_path = installation_path.join(java_executable_path);
    let java_path = match java_path.to_str() {
        Some(java_path) => java_path,
        None => return None,
    };

    Some(java_path.to_string())
}

fn get_launcher_executable_path() -> Option<String> {
    let executable_path = get_current_executable_path();

    if executable_path.is_none() {
        return None;
    }

    let executable_path = executable_path.unwrap();
    let installation_path = Path::new(executable_path.as_str());

    let executable_path = if cfg!(target_os = "windows") {
        Path::new("MikuPush.exe")
    } else {
        Path::new("MikuPush")
    };

    let launcher_path = installation_path.join(executable_path);
    let launcher_path_str = match launcher_path.to_str() {
        Some(path) => path,
        None => return None,
    };

    Some(String::from(launcher_path_str))
}

pub fn is_running() -> bool {
    let client = reqwest::blocking::Client::builder()
        .timeout(Duration::from_secs(1))
        .build()
        .expect("Could not build http client");

    let response = match client.get("http://127.0.0.1:49152/ping").send() {
        Ok(result) => result,
        Err(_) => return false,
    };

    response.status() == reqwest::StatusCode::OK && response.text().unwrap_or_default() == "pong"
}

pub fn restore_window() -> Result<(), &'static str> {
    let client = reqwest::blocking::Client::builder()
        .timeout(Duration::from_secs(1))
        .build()
        .expect("Could not build http client");

    let response = match client.get("http://127.0.0.1:49152/restore").send() {
        Ok(result) => result,
        Err(_) => return Err("Could not send restore window request"),
    };

    if response.status() != reqwest::StatusCode::NO_CONTENT {
        Err("Could not restore window")
    } else {
        Ok(())
    }
}

pub fn request_upload(path: &str) -> Result<(), &str> {
    let client = reqwest::blocking::Client::builder()
        .timeout(Duration::from_secs(3))
        .build()
        .expect("Could not build http client");

    let body = String::from(path);
    let request = client.post("http://127.0.0.1:49152/upload")
        .body(body)
        .header(reqwest::header::CONTENT_TYPE, "text/plain")
        .header(reqwest::header::ACCEPT, "text/plain");

    let response = match request.send() {
        Ok(result) => result,
        Err(_) => return Err("Could not send upload request"),
    };

    if response.status() != reqwest::StatusCode::NO_CONTENT {
        Err("Upload request failed")
    } else {
        Ok(())
    }
}

pub fn launch_java_command_blocking(args: Vec<String>) -> i32 {
    let java_executable = get_java_executable_path()
        .expect("Could not get java executable path");

    println!("Java executable: {}", java_executable);

    let mut child = Command::new(java_executable)
        .args(args)
        .creation_flags(CREATE_NO_WINDOW)
        .spawn()
        .expect("Could not spawn child process");

    let status = child.wait()
        .expect("Could not wait on child process");

    status.code().expect("Could not get exit status")
}

pub fn start_miku_push_silently() {
    let launcher_path = get_launcher_executable_path()
        .expect("Could not get launcher executable path");

    let process = Command::new(launcher_path)
        .args(["--tray"])
        .creation_flags(CREATE_NO_WINDOW)
        .stdin(Stdio::null())
        .stdout(Stdio::null())
        .stderr(Stdio::null())
        .spawn()
        .expect("Could not spawn child process");

    println!("MikuPush launched silently with PID {}", process.id());
}