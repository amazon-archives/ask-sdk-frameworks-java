package ${package};

import ${package}.controllers.${skillName}Controller;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.types.intent.CancelIntent;
import com.amazon.ask.interaction.types.intent.HelpIntent;
import com.amazon.ask.interaction.types.intent.StopIntent;
import com.amazon.ask.mvc.MvcSdkModule;
import com.amazon.ask.mvc.SkillModule;
import com.amazon.ask.mvc.view.nashorn.NashornViewResolver;

public class ${skillName}Module implements SkillModule {
    @Override
    public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
        mvcBuilder
            .addController(new ${skillName}Controller())
            .addViewResolver(NashornViewResolver.builder()
                .withResourceClass(getClass())
                .withPrefix("views/")
                .withRenderFunction("render")
                .build());
    }

    @Override
    public void buildModel(Model.Builder modelBuilder) {
        modelBuilder
            .intent(CancelIntent.class)
            .intent(HelpIntent.class)
            .intent(StopIntent.class);
    }
}