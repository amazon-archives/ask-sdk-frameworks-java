package ${package}.controllers;

import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.ask.interaction.types.intent.CancelIntent;
import com.amazon.ask.interaction.types.intent.HelpIntent;
import com.amazon.ask.interaction.types.intent.StopIntent;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.view.ModelAndView;

public class ${skillName}Controller {

    @RequestMapping(types = LaunchRequest.class)
    public ModelAndView launch() {
        ModelAndView mv = new ModelAndView("basic");
        mv.put("speech", "Welcome to ${skillName}. This is where you will greet users of your skill.");
        mv.put("reprompt", "What would you like to do?");
        return mv;
    }

    @IntentMapping(type = HelpIntent.class)
    public ModelAndView help() {
        ModelAndView mv = new ModelAndView("basic");
        mv.put("speech", "Welcome to ${skillName}. This is where users will be directed when asking for help.");
        mv.put("reprompt", "What would you like to do?");
        return mv;
    }

    @IntentMapping(type = StopIntent.class)
    public ModelAndView stop() {
        ModelAndView mv = new ModelAndView("basic");
        mv.put("speech", "Bye!");
        return mv;
    }

    @IntentMapping(type = CancelIntent.class)
    public ModelAndView cancel() {
        ModelAndView mv = new ModelAndView("basic");
        mv.put("speech", "Bye!");
        return mv;
    }

    @RequestMapping(types = SessionEndedRequest.class)
    public void sessionEnd(SessionEndedRequest request) {
        // any cleanup logic goes here
        System.out.println(String.format("Session ended with reason %s", request.getReason()));
    }

}