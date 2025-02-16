mod common;

use std::env;
use common::{
    launch_java_command_blocking,
    is_running,
    restore_window
};

fn main() {
    if is_running() {
        return restore_window().unwrap();
    }

    let args = env::args().collect::<Vec<String>>();
    let mut command_args = Vec::from([
        String::from("-jar"),
        String::from("mikupush.jar"),
        String::from("ui")
    ]);

    for arg in args {
        command_args.push(arg);
    }

    std::process::exit(launch_java_command_blocking(command_args))
}
