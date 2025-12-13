package com.example.footsale.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    /**
     * Crea un archivo temporal en la caché de la app a partir de un URI de contenido.
     * Esto es necesario para poder subir archivos seleccionados desde la galería.
     */
    public static File getFile(Context context, Uri uri) {
        if (uri == null) return null;

        try {
            // Crear un archivo temporal en el directorio de caché
            File destinationFile = new File(context.getCacheDir(), "upload_temp_file");

            // Copiar el contenido del URI al archivo de destino
            try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
                 OutputStream outputStream = new FileOutputStream(destinationFile)) {

                if (inputStream == null) return null;

                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
            }
            return destinationFile;
        } catch (Exception e) {
            Log.e("FileUtils", "Error al crear el archivo temporal", e);
            return null;
        }
    }
}
