package com.javainuse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Collection;

@RestController
public class TestController {


	TokenStore tokenStore;

	@Resource(name="tokenStore")
	protected void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	@RequestMapping("/test")
	public String test() {

		System.out.println("Here I ammm 111");
		Collection<OAuth2AccessToken> tokens =  tokenStore.findTokensByClientId("basic-client");

		System.out.println("Here I ammm 222");
		for (OAuth2AccessToken authToken : tokens) {
			System.out.println("Here I ammm "+authToken.toString());
		}

		return "Hello World";
	}

	@RequestMapping(value = "/username", method = RequestMethod.GET)
	@ResponseBody
	public String currentUserName(Principal principal) {
		return principal.getName();
	}

	@RequestMapping(value = "/usernameauth", method = RequestMethod.GET)
	@ResponseBody
	public String currentUserName(Authentication authentication) {
		return authentication.getName();
	}

	@GetMapping("/whoami")
	public String whoami(@AuthenticationPrincipal(expression="name") String name) {
		return name;
	}

//	@GetMapping(path = "/api/me", produces = "application/json" )
//	public String me() {
//		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		return username;
//	};
}
