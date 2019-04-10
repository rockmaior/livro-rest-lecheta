package br.com.livro.domain;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;
import com.google.common.collect.ImmutableList;

public class CloudStorageUtil {
	private Storage client;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private final String applicationName;
	private static HttpTransport httpTransport;

	public CloudStorageUtil(String applicationName) {
		super();
		this.applicationName = applicationName;
	}

	// Conecta no Google APIs
	public void connect(String accountId, File p12File) throws Exception {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		Credential credential = authorize(accountId, p12File);
		client = new Storage.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(applicationName)
				.build();
	}

	// Autoriza o acesso com Server-Side Authorizarion"
	private Credential authorize(String accountId, File p12File) throws Exception {
		Set<String> scopes = new HashSet<String>();
		scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
		scopes.add(StorageScopes.DEVSTORAGE_READ_ONLY);
		scopes.add(StorageScopes.DEVSTORAGE_READ_WRITE);

		// Autoriza a aplicacao
		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY).setServiceAccountId(accountId)
				.setServiceAccountPrivateKeyFromP12File(p12File).setServiceAccountScopes(scopes).build();
		return credential;
	}

	// Retorna um bucket

	public Bucket getBucket(String bucketName) throws IOException {
		Storage.Buckets.Get getBucket = client.buckets().get(bucketName);
		getBucket.setProjection("full");
		Bucket bucket = getBucket.execute();
		return bucket;
	}

	// Lista os arquivos do bucket

	public List<StorageObject> getBucketFiles(Bucket bucket) throws IOException {
		Storage.Objects.List listObjects = client.objects().list(bucket.getName());
		com.google.api.services.storage.model.Objects objects;
		ArrayList<StorageObject> all = new ArrayList<StorageObject>();
		do {
			objects = listObjects.execute();
			List<StorageObject> items = objects.getItems();
			if (null == items) {
				// sem arquivos
				return all;
			}
			// addiciona os arquivos
			all.addAll(items);
			listObjects.setPageToken(objects.getNextPageToken());
		} while (null != objects.getNextPageToken());

		return all;
	}

	// Faz upload do arquivo no storage
	public StorageObject upload(String bucketName, File file, String contentType, String projectId) throws IOException {
		if (client == null) {
			throw new IOException("Cloud Storage API nao esta conectada");
		}
		if (file == null || !file.exists() || !file.isFile()) {
			throw new FileNotFoundException("Arquivo nao encontrado");
		}

		// Nome do arquivo
		String fileName = file.getName();
		// Le o arquivo
		InputStream inputStream = new FileInputStream(file);
		long byteCount = file.length();
		InputStreamContent mediaContent = new InputStreamContent(contentType, inputStream);
		mediaContent.setLength(byteCount);

		// Configura acesso ao ACL = Access List Control

		ImmutableList<ObjectAccessControl> acl = ImmutableList.of(
				new ObjectAccessControl().setEntity(String.format("project-owners-%s", projectId)).setRole("OWNER"),
				new ObjectAccessControl().setEntity(String.format("project-editors-%s", projectId)).setRole("OWNER"),
				new ObjectAccessControl().setEntity(String.format("project-viewers-%s", projectId)).setRole("READER"),
				new ObjectAccessControl().setEntity("allUsers").setRole("READER")// URL publica
		);

		// Configura o objeto
		StorageObject obj = new StorageObject();
		obj.setName(fileName); // nome
		obj.setContentType(contentType); // tipo do conteudo
		obj.setAcl(acl); // permissoes - foi configurado para URL publica

		// Insere o objeto no cloud storage

		Storage.Objects.Insert insertObject = client.objects().insert(bucketName, obj, mediaContent);

		if (mediaContent.getLength() > 0 && mediaContent.getLength() <= 2 * 1000 * 1000 /* 2MB */) {
			// Utiliza o direct upload para arquivos pequenos
			insertObject.getMediaHttpUploader().setDirectUploadEnabled(true);
		}

		// Executa a API

		insertObject.execute();
		return obj;
	}
}
