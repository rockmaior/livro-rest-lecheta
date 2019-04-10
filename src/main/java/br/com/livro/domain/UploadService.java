package br.com.livro.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.google.api.services.storage.model.StorageObject;
import com.google.common.io.Files;

@Component
public class UploadService {
	/*
	 * public String upload(String fileName, InputStream in) throws IOException { if
	 * (fileName == null || in == null) { throw new
	 * IllegalArgumentException("Parâmetros inválidos"); }
	 * 
	 * // Temporary folder of JVM
	 * 
	 * File tmpDir = new File(System.getProperty("java.io.tmpdir"), "carros"); if
	 * (!tmpDir.exists()) { // Cria a pasta carro se nao existe tmpDir.mkdir(); } //
	 * cria o arquivo File file = new File(tmpDir, fileName);
	 * 
	 * // Abre o stream para escrever no arquivo FileOutputStream out = new
	 * FileOutputStream(file);
	 * 
	 * IOUtils.copy(in, out); IOUtils.closeQuietly(out);
	 * 
	 * // Retorna o caminho do arquivo
	 * 
	 * String path = file.getAbsolutePath();
	 * 
	 * return path;
	 * 
	 * }
	 * 
	 */

	private static final String PROJECT_ID = "163261512753";
	private static final String ACCOUNT_ID = "163261512753-compute@developer.gserviceaccount.com";
	private static final String APP_NAME = "Teste";
	private static final String BUCKET_NAME = "livro_rest_regis";

	public String upload(String fileName, InputStream in) throws Exception {
		// Salva o arquivo na pasta temporaria da JVM
		File file = saveToTmpDir(fileName, in);

		// Faz o upload para o Cloud Storage
		String url = uploadToCloudStorage(file);

		// Retorna o URL do arquivo

		return url;
	}

	private String uploadToCloudStorage(File file) throws Exception {
		String s = System.getProperty("p12File");
		if (s == null) {
			throw new IOException("Erro no servidor");

		}

		File p12File = new File(s);

		if (!p12File.exists()) {
			throw new IOException("Erro no servidor");
		}

		// Conecta no Cloud Storage

		CloudStorageUtil c = new CloudStorageUtil(APP_NAME);
		c.connect(ACCOUNT_ID, p12File);

		// Upload
		String fileName = file.getName();
		String contentType = getContentType(fileName);
		String storageProjectId = "163261512753";
		StorageObject obj = c.upload(BUCKET_NAME, file, contentType, storageProjectId);

		if (obj == null) {
			throw new IOException("Erro ao fazer o upload");
		}

		// Retorna a URL pública
		String url = String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, obj.getName());

		return url;
	}

	private String getContentType(String fileName) {
		String ext = Files.getFileExtension(fileName);
		if ("png".equals(ext)) {
			return "image/png";
		} else if ("jpg".equals(ext) || "jpeg".equals(ext)) {
			return "image/jpg";
		} else if ("gif".equals(ext)) {
			return "image/gif";
		}
		return "text/plain";
	}

	private File saveToTmpDir(String fileName, InputStream in) throws FileNotFoundException, IOException {

		if (fileName == null || in == null) {
			throw new IllegalArgumentException("Parâmetros inválidos");
		}

		// Temporary folder of JVM

		File tmpDir = new File(System.getProperty("java.io.tmpdir"), "carros");
		if (!tmpDir.exists()) {
			// Cria a pasta carro se nao existe
			tmpDir.mkdir();
		}
		// cria o arquivo
		File file = new File(tmpDir, fileName);

		// Abre o stream para escrever no arquivo
		FileOutputStream out = new FileOutputStream(file);

		IOUtils.copy(in, out);
		IOUtils.closeQuietly(out);

		return file;
	}

}
