package com.example.demo.user;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.demo.utils.Constants.*;

public class UserAuth {

    private AWSCognitoIdentityProvider createCognitoClient() {

        AWSCredentials cred = new BasicAWSCredentials(ACCESS_KEY, SEC_ACCESS_KEY);
        AWSCredentialsProvider credProvider = new AWSStaticCredentialsProvider(cred);
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(credProvider)
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }

    public SignUpResult signUp(String name, String email, String password) {
        SignUpRequest request = new SignUpRequest().withClientId(COGNITO_CLIENT_ID)
                .withUsername(email)
                .withPassword(password)
                .withUserAttributes(
                        new AttributeType()
                                .withName("email")
                                .withValue(email));
        SignUpResult result = createCognitoClient().signUp(request);
        return result;
    }

    public ConfirmSignUpResult confirmSignUp(String email, String confirmationCode) {
        ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest()
                .withClientId(COGNITO_CLIENT_ID)
                .withUsername(email)
                .withConfirmationCode(confirmationCode);
        return createCognitoClient().confirmSignUp(confirmSignUpRequest);
    }


    public AuthenticationResultType login(String email, String password) {
        Map<String, String> authParams = new LinkedHashMap<String, String>() {{
            put("USERNAME", email);
            put("PASSWORD", password);
        }};

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .withUserPoolId(COGNITO_POOL_ID)
                .withClientId(COGNITO_CLIENT_ID)
                .withAuthParameters(authParams);
        AdminInitiateAuthResult authResult = createCognitoClient().adminInitiateAuth(authRequest);
        AuthenticationResultType resultType = authResult.getAuthenticationResult();

        return  resultType;
//        return new LinkedHashMap<String, String>() {{
//            put("idToken", resultType.getIdToken());
//            put("accessToken", resultType.getAccessToken());
//            put("refreshToken", resultType.getRefreshToken());
//            put("message", "Successfully login");
//        }};

    }

}
