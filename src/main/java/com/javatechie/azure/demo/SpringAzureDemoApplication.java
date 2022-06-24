package com.javatechie.azure.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.PasswordProfile;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@SpringBootApplication
@RestController
public class SpringAzureDemoApplication {

	@GetMapping("Admin")
	@ResponseBody
	@PreAuthorize("hasAuthority('APPROLE_Admin')")
	public String Admin() {
		return "Admin message";
	}
	
	@GetMapping("/message")
	public String message(){
		System.out.println("creating user.... wait...");
		createUser();
		return "Congrats ! your application deployed successfully in Azure Platform by mshastri. !!!!!!";
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringAzureDemoApplication.class, args);
	}
	
	

	public void createUser() {
		final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
				.clientId("296e8d19-e615-4d9c-8fb7-8bb271bf6028")
				.clientSecret("Lrc8Q~XBmsvluvwoWGJZz1Ke4.OlemVr1QHv5cSt")
				.tenantId("2af3bcab-49bd-4adb-a08b-d729262da2b7").build();

		List<String> scopes = new ArrayList<>();
		scopes.add("https://graph.microsoft.com/.default");

		final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes,
				clientSecretCredential);

		final GraphServiceClient graphClient = GraphServiceClient.builder()
				.authenticationProvider(tokenCredentialAuthProvider).buildClient();

		// User me = graphClient.me().buildRequest().get();

		User user = new User();
		user.accountEnabled = true;
		user.displayName = "MB Shastri";
		user.mailNickname = "mshastri";
		user.userPrincipalName = "mshastri@contoso.onmicrosoft.com";
		PasswordProfile passwordProfile = new PasswordProfile();
		passwordProfile.forceChangePasswordNextSignIn = true;
		passwordProfile.password = "xWwvJ]6NMw+bWH-d";
		user.passwordProfile = passwordProfile;
		System.out.println("User is " + user);
		User newuser = graphClient.users().buildRequest().post(user);

		System.out.println("New user is " + newuser);
	}

}
