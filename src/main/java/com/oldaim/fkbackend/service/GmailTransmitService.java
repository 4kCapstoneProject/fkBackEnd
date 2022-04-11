package com.oldaim.fkbackend.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.oldaim.fkbackend.controller.dto.ReturnInfoDto;
import com.oldaim.fkbackend.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class GmailTransmitService {

    private final UserService userService;

    @Value("${gmail.credential_path}") private String CREDENTIALS_FILE_PATH;
    @Value("${gmail.admin_user_id}") private String ADMIN_USER_ID;
    @Value("${gmail.token_directory_path}") private String TOKENS_DIRECTORY_PATH ;

    private static final List<String> SCOPES = List.of(GmailScopes.GMAIL_SEND, GmailScopes.MAIL_GOOGLE_COM);
    private static final String APPLICATION_NAME = "Gmail API For 4K";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String RETURN_INFO_URL = "http://localhost:8080/api/return/download";

    public String sendEmailForUser(ReturnInfoDto returnInfoDto, UserDetails userDetails)
            throws GeneralSecurityException, IOException, MessagingException {

        Gmail service = this.getService();

        String toUser = returnUserEmail(userDetails);

        String subject = "분석이 완료되었습니다.";

        String bodyText = makeBodyText(returnInfoDto);

        MimeMessage mimeMessage = createEmail( toUser, ADMIN_USER_ID, subject, bodyText);

        sendMessage(service,ADMIN_USER_ID,mimeMessage);

        return toUser + "에게 분석 내용을 전달하였습니다.";

    }

    private String returnUserEmail(UserDetails userDetails){

        User user = userService.findByUserId(userDetails.getUsername())
                .orElseThrow(()->new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        return user.getUserEmail();
    }

    private String makeBodyText(ReturnInfoDto returnInfoDto){

        String bodyPart = returnInfoDto.getPersonName() + " 타겟의 분석이 완료되었습니다." +"\n";
        String bodyURL = "\n" + RETURN_INFO_URL +"에서 분석 결과를 보실 수 있습니다.";

        return bodyPart + bodyURL;

    }

    // Gmail 서비스 객체 생성
    public Gmail getService() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    //인증 정보 생성
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        File file = new File(CREDENTIALS_FILE_PATH);

        InputStream in = new BufferedInputStream(new FileInputStream(file));

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(ADMIN_USER_ID);
    }

    // Email 생성
   private MimeMessage createEmail(String to, String from, String subject, String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);

        return email;
    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        emailContent.writeTo(buffer);

        byte[] bytes = buffer.toByteArray();

        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

        Message message = new Message();

        message.setRaw(encodedEmail);

        return message;
    }

    private void sendMessage(Gmail service, String userId, MimeMessage emailContent) throws MessagingException, IOException {

        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());

    }

}
