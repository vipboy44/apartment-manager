package poly.com.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Component
public class FireBase {

    private class Constants {
        public static final String PROJECT_ID = "apartment-management";
        public static final String BUCKET_NAME = "apartment-management-15f74.appspot.com";
        public static final String SERVICE_ACCOUNT_KEY = "firebase.json";
    }

    private final String bucketName = Constants.BUCKET_NAME;

    private Storage storage;

    public FireBase() {
        try {
            initStorage();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void initStorage() throws IOException {
        Resource resource = new ClassPathResource(Constants.SERVICE_ACCOUNT_KEY);
//        try (FileInputStream serviceAccount = new FileInputStream(FireBase.class.getResource(Constants.SERVICE_ACCOUNT_KEY).getFile())) {
        try (InputStream serviceAccount = resource.getInputStream()) {
            this.storage = StorageOptions.newBuilder()
                    .setProjectId(Constants.PROJECT_ID)
                    .setCredentials(ServiceAccountCredentials.fromStream(serviceAccount))
                    .build()
                    .getService();
        }
    }

    public void uploadImg(String folderName, File file) throws IOException {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, folderName + "/" + file.getName()))
                .setContentType("image/jpeg")
                .build();

        try (WriteChannel writer = storage.writer(blobInfo);
             InputStream inputStream = new FileInputStream(file)
        ) {
            byte[] buffer = new byte[5120000];
            int limit;
            while ((limit = inputStream.read(buffer)) >= 0) {
                writer.write(ByteBuffer.wrap(buffer, 0, limit));
            }
        }
    }

    public void uploadImg(String folderName, MultipartFile file, String newName) throws IOException {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, folderName + "/" + newName))
                .setContentType("image/jpeg")
                .build();

        try (WriteChannel writer = storage.writer(blobInfo);
             InputStream inputStream = file.getInputStream()
        ) {
            byte[] buffer = new byte[5120000];
            int limit;
            while ((limit = inputStream.read(buffer)) >= 0) {
                writer.write(ByteBuffer.wrap(buffer, 0, limit));
            }
        }
    }

    public BufferedImage downloadImg(String folderName, String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, folderName + "/" + fileName);
        byte[] buffer = storage.readAllBytes(blobId);
        return ImageIO.read(new ByteArrayInputStream(buffer));

    }

    // delete image
    public void DeleteImg(String folderName, String fileName) {
        BlobId blobId = BlobId.of(bucketName, folderName + "/" + fileName);
        storage.delete(blobId);
    }

}