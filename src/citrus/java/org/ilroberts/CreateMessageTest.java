package org.ilroberts;


import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.TestNGCitrusTestBuilder;
import org.testng.annotations.Test;

@Test
public class CreateMessageTest extends TestNGCitrusTestBuilder {

    public void setup_variables() {
        variable("version", "1.0");
        variable("reference", "1234546");
        variable("userId", "2");
        variable("physicianId", "12");
        variable("organizationId", "2");
        variable("topicId", "1");
        variable("bodyText", "this is a new message");
        variable("customerCareComment", "false");
    }

    @CitrusTest(name = "createMessage")
    public void createMessage() {

        setup_variables();
        send("physicianPortalHttpClient")
                .payload("<messageCreateRequest>"
                        + "<thread>"
                        + "<physician><id>${physicianId}</id></physician>"
                        + "<organization><id>${organizationId}</id></organization>"
                        + "<topic><id>${topicId}</id></topic>"
                        + "</thread>"
                        + "<message>"
                        + "<body>${bodyText}</body>"
                        + "<customercareComment>${customerCareComment}</customercareComment>"
                        + "</message></messageCreateRequest>")
                .header("citrus_endpoint_uri", "http://localhost:8080/Message/create/${version}/${reference}/${userId}");

        receive("physicianPortalHttpClient")
                .validate("messageCreateResponse/responseHeader/statusCode", "0000")
                .validate("messageCreateResponse/responseHeader/referenceId", "${reference}")
                .header("citrus_http_status_code", "200");
    }
}
