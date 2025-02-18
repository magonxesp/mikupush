class ObservableFileUpload {
  final String fileName;
  final String mimeType;
  double _speed = 0;
  double _progress = 0;
  bool _isInProgress = false;
  bool _isFinishedWithErrors = false;
  bool _isFinishedSuccessfully = false;
  void Function() _callback = _defaultCallback;

  ObservableFileUpload(this.fileName, this.mimeType);

  static _defaultCallback() {}

  void setSpeed(double speed) {
    _speed = speed;
    notify();
  }

  void notify() {
    _callback();
  }

  void onChange(void Function() callback) {
    _callback = callback;
  }
}