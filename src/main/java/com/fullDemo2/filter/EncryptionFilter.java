package com.fullDemo2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//@Component
public class EncryptionFilter extends AbstractHttpMessageConverter<Object> {

	@Autowired
	private ObjectMapper objectMapper;

	public EncryptionFilter() {
		super(MediaType.APPLICATION_JSON, new MediaType("application", "*+json", StandardCharsets.UTF_8));
	}

	/*
	 * True to encrypt the data
	 */
	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		System.out.println("--------------In ReadInternal ");

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (request.getServletPath().endsWith("/test/encrypt") ||
		  (request.getServletPath().endsWith("/user/login")) ||
		  (request.getServletPath().endsWith("/user/register")) ) {
			// This is a plain request to encrypt it
			return objectMapper.readValue(inputMessage.getBody(), clazz);
		} else {
			// Expect that incoming message is encrypted -> Need to decrypt it
			return objectMapper.readValue(decrypt(inputMessage.getBody()), clazz);
		}
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		if (request.getServletPath().endsWith("/test/decrypt")) {
//		if (request.getServletPath().endsWith("/user/login")) {
			outputMessage.getBody().write(objectMapper.writeValueAsBytes(o));
		} else {
			outputMessage.getBody().write(encrypt(objectMapper.writeValueAsBytes(o)));
		}

	}

	private byte[] encrypt(byte[] bytesToEncrypt) {
		// do your encryption here
		String apiJsonResponse = new String(bytesToEncrypt);
		String encryptedString = AES.encryptMyAES(apiJsonResponse);
		System.out.println("----------AES EncryptJSON-----------------------");
		System.out.println(encryptedString);
		System.out.println("---------------------------------");
		if (encryptedString != null) {
			// sending encoded json response in data object as follows

			Map<String, String> hashMap = new HashMap<>();
			hashMap.put("data", encryptedString);
			JSONObject jsonObject = new JSONObject(hashMap);

			return jsonObject.toString().getBytes();
		} else
			return bytesToEncrypt;
	}

	private InputStream decrypt(InputStream inputStream) {

		System.out.println("--------------   In ReadInternal  --------------------- ");

		// this is API request params
		StringBuilder requestParamString = new StringBuilder();
		String decryptRequestString = null;
		try (Reader reader = new BufferedReader(
				new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
			int c;
			while ((c = reader.read()) != -1) {
				requestParamString.append((char) c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			System.out.println("RequestParamString:" + requestParamString);
			System.out.println("-----------------------------------------------");
			// replacing "/n" if available in request param json string
			// reference request: {"data":"ThisIsEncryptedStringWithExpiryTime"}

			JSONObject requestJsonObject = new JSONObject(requestParamString.toString().replace("\n", ""));

			System.out.println("JSONObject: " + requestJsonObject);
			System.out.println("-----------------------------------------------");

			decryptRequestString = AES.decrypt(requestJsonObject.getString("data"));
			System.out.println("decryptRequestString: " + decryptRequestString);

			JSONObject decryptedJsonObject = new JSONObject(decryptRequestString.replace("\n", ""));
			System.out.println("------------------------------------------------");
			System.out.println("decryptedJsonObject:" + decryptedJsonObject);

			InputStream targetStream = new ByteArrayInputStream(decryptRequestString.getBytes());

			return targetStream;
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		return null;

	}

}
