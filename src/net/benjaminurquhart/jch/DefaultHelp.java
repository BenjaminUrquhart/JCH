package net.benjaminurquhart.jch;

import java.util.List;

@SlashCommand
public class DefaultHelp<T> extends AbstractCommand<T> {
	
	public DefaultHelp() {
		super("help");
	}
	
	@Override
	public void handle(CommandEvent event, T self) {
		List<AbstractCommand<T>> commands = this.getHandler().getRegisteredCommands();
		String out = "```";
		for(AbstractCommand<?> command : commands){
			if(command.hide() || command.isMessageInteraction()){
				continue;
			}
			out += command.getHelpMenu().replace("Usage:", "").trim() + " - " + command.getDescription() + "\n";
		}
		out += "```";
		event.startReply(true).addContent(out).queue();
	}
	@Override
	public String getDescription() {
		return "does all the work";
	}

}
