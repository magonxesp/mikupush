import 'package:http/http.dart' as http;
import 'dart:io';

final Uri uploadUri = Uri.https('mikupush.io', 'upload');

// TODO: devolver un ObservableFileUpload
// actualizar el estado del widget del progreso con esta clase
// crear una cola para encolar ficheros a subir y subirlos uno a uno

Future<void> uploadFileWithProgress(String filePath) async {
  final file = File(filePath);
  final fileStream = file.openRead();
  final fileLength = await file.length();

  int totalBytesSent = 0;
  int lastTime = DateTime.now().millisecondsSinceEpoch;

  Stream<List<int>> progressStream = fileStream.map((List<int> chunk) {
    totalBytesSent += chunk.length;

    int currentTime = DateTime.now().millisecondsSinceEpoch;
    int timeDiff = currentTime - lastTime;

    if (timeDiff > 1000) {
      double speed = (totalBytesSent / 1024) / (timeDiff / 1000); // KB/s
      double progress = (totalBytesSent / fileLength) * 100;
      print('Progreso: ${progress.toStringAsFixed(2)}% - Velocidad de subida: ${speed.toStringAsFixed(2)} KB/s');

      lastTime = currentTime;
      totalBytesSent = 0;
    }

    return chunk;
  });

  final headers = {
    'Content-Type': '',
  };

  final response = await http.post(
    uploadUri,
    headers: headers,
    body: progressStream,
  );

  if (response.statusCode == 200) {
    print('Archivo subido correctamente!');
  } else {
    print('Error al subir el archivo: ${response.statusCode}');
  }
}
