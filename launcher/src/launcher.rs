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

    let extra_args = args[1..].to_vec();
    for arg in extra_args {
        command_args.push(arg);
    }

    println!("Extra args {:?}", command_args);

    std::process::exit(launch_java_command_blocking(command_args))
}
