#![windows_subsystem = "windows"]
mod common;

use common::{
    is_running,
    request_upload,
    start_miku_push_silently
};
use std::env;
use std::process::ExitCode;

pub fn main() -> ExitCode {
    if !is_running() {
        start_miku_push_silently();

        let mut program_started = false;

        for _ in 0..3 {
            if is_running() {
                program_started = true;
                break;
            }
        }

        if !program_started {
            println!("Can't run MikuPush, it is not possible to send upload request!");
            return ExitCode::FAILURE;
        }
    }

    let paths = env::args().collect::<Vec<String>>();
    let upload_paths = paths[1..].to_vec();

    for path in upload_paths {
        if let Err(e) = request_upload(&path) {
            println!("Failed to request upload for {}: {}", path, e);
        }
    }

    ExitCode::SUCCESS
}
