class SocketAckMissingException implements Exception {
  @override
  String toString() => 'The socket server not responded with the ack message';
}
