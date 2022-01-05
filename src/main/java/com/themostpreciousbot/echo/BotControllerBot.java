package echobothitss;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.connector.authentication.MicrosoftAppCredentials;
import com.microsoft.bot.integration.BotFrameworkHttpAdapter;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ConversationParameters;
import com.microsoft.bot.schema.ConversationReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotControllerBot {
    private final BotFrameworkHttpAdapter adapter;
    @Autowired
    public BotControllerBot(BotFrameworkHttpAdapter adapter){
        this.adapter = adapter;
    }
    @CrossOrigin
    @PostMapping("/teste")
    public CompletableFuture<Void> send(){
        MicrosoftAppCredentials credentials = new MicrosoftAppCredentials("4b146aaa-f023-426c-90fe-9f7445718405", "echoBotHitss123456");
        Activity message = MessageFactory.text("Teste");
        ObjectNode channelData = JsonNodeFactory.instance.objectNode();
        channelData.set(
            "channel",
            JsonNodeFactory.instance.objectNode()
                .set("id", JsonNodeFactory.instance.textNode("19:fe8e94b560ae42ac8e3b721dd9f71e68@thread.tacv2"))
        );

        ConversationParameters conversationParameters = new ConversationParameters();
        conversationParameters.setIsGroup(true);
        conversationParameters.setActivity(message);
        conversationParameters.setChannelData(channelData);

        return adapter.createConversation("19:fe8e94b560ae42ac8e3b721dd9f71e68@thread.tacv2", 
        "https://4134-179-55-80-198.ngrok.io/api/messages", 
        credentials,
        conversationParameters,
            (tc) -> {
                ConversationReference reference = tc.getActivity().getConversationReference();
                return tc.getAdapter().continueConversation(
                    "4b146aaa-f023-426c-90fe-9f7445718405",
                    reference,
                    (continue_tc) -> continue_tc.sendActivity(
                        MessageFactory.text(
                            "This will be the first response to the new thread"
                        )
                    ).thenApply(resourceResponse -> null)
                );
            }
        ).thenApply(started -> null);
    }
}
