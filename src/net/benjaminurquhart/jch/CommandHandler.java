package net.benjaminurquhart.jch;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandHandler<T> extends ListenerAdapter{

	private T self;
	private String prefix, owner;
	private HashMap<String, Command<T>> commands;
	
	@SuppressWarnings("unchecked")
	public CommandHandler(T self, String prefix, String ownerID, String commandsPackage){
		if(self == null || prefix == null) {
			throw new IllegalArgumentException("Self and prefix cannot be null!");
		}
		this.self = self;
		this.prefix = prefix;
		this.owner = ownerID;
		this.commands = new HashMap<>();
		Reflections reflections = commandsPackage == null ? new Reflections() : new Reflections(commandsPackage);
		reflections.getSubTypesOf(Command.class).forEach((cls) -> {
			try{
				this.registerCommand(cls.getDeclaredConstructor().newInstance());
			}
			catch(Exception e){
				e.printStackTrace();
			}
		});
	}
	public CommandHandler(T self, String prefix, String ownerID) {
		this(self, prefix, ownerID, null);
	}
	public CommandHandler(T self, String prefix) {
		this(self, prefix, null, null);
	}
	public void registerCommand(Command<T> command){
		command.setHandler(this);
		commands.put(command.getName(), command);
		for(String alias : command.getAliases()) {
			commands.put(alias, command);
		}
		System.out.println("Registered " + (command.hide() ? "private " : "") + "command " + command.getName());
	}
	public List<Command<T>> getRegisteredCommands(){
		return commands.values().stream().distinct().collect(Collectors.toList());
	}
	public String getPrefix(){
		return prefix;
	}
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){
		if(!event.getChannel().canTalk()){
			return;
		}
		String msg = event.getMessage().getContentRaw().toLowerCase();
		if(event.getAuthor().isBot() || !msg.startsWith(prefix)){
			return;
		}
		String cmd = msg.substring(prefix.length()).trim();
		if(cmd.contains(" ")){
			cmd = cmd.split(" ")[0];
		}
		Command<T> command = commands.get(cmd);
		if(command != null){
			try{
				command.handle(event, self);
			}
			catch(Exception e){
				event.getChannel().sendMessage("Oh no! Something went wrong while executing that command!\nThis incident has been reported.\n" + e).queue();
				if(this.owner == null) {
					this.owner = event.getJDA().asBot().getApplicationInfo().complete().getOwner().getId();
				}
				User owner = event.getJDA().getUserById(this.owner);
				if(owner != null){
					owner.openPrivateChannel().queue(
					(channel) ->{
						String out = "```" + e.toString();
						for(StackTraceElement trace : e.getStackTrace()){
							out += "\n" + trace.toString();
						}
						if(out.length() > 1990){
							out = out.substring(0, 1990) + "...";
						}
						channel.sendMessage(out + "```").queue((m) -> {},
						(error) ->{
							event.getChannel().sendMessage("Failed to report the incident!\n" + error).queue();
						});
						channel.sendMessage("Command: `" + event.getMessage().getContentRaw() + "`").queue((m) -> {}, (error) -> {});
					});
				}
			}
			return;
		}
	}
}
