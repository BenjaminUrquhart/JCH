package net.benjaminurquhart.jch;

import java.util.List;

import net.dv8tion.jda.api.entities.TextChannel;

@SlashCommand
public class DefaultHelp<T> extends AbstractCommand<T> {
	
	public DefaultHelp() {
		super("help");
	}
	
	@Override
	public void handle(CommandEvent event, T self) {
		TextChannel channel = event.getChannel();
		List<AbstractCommand<T>> commands = this.getHandler().getRegisteredCommands();
		String out = "```";
		for(AbstractCommand<?> command : commands){
			if(command.hide()){
				continue;
			}
			out += command.getHelpMenu().replace("Usage:", "").trim() + " - " + command.getDescription() + "\n";
		}
		out += "```";
		channel.sendMessage(out).queue();
	}
	@Override
	public String getDescription() {
		return "does all the work";
	}

}
