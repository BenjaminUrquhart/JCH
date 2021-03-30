package net.benjaminurquhart.jch;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandHandler<T> extends ListenerAdapter {

	private T self;
	private JDA jda;
	private String prefix, owner;
	private boolean mentionPrefix;
	private Map<String, Command<T>> commands;
	
	private Map<String, Future<?>> ratelimits;
	private TimeUnit unit;
	private int limit;
	
	private ScheduledExecutorService limitService;
	
	private DefaultHelp<T> defaultHelpCmd = null;
	
	@SuppressWarnings("unchecked")
	public CommandHandler(T self, String prefix, String ownerID, String commandsPackage) {
		if(self == null) {
			throw new IllegalArgumentException("Self cannot be null!");
		}
		this.self = self;
		this.prefix = prefix;
		this.owner = ownerID;
		this.commands = new HashMap<>();
		this.mentionPrefix = prefix == null;
		Reflections reflections = commandsPackage == null ? new Reflections() : new Reflections(commandsPackage);
		reflections.getSubTypesOf(Command.class).forEach((cls) -> {
			try{
				this.registerCommand(cls.getDeclaredConstructor().newInstance());
			}
			catch(Exception e){
				e.printStackTrace();
			}
		});
		this.defaultHelpCmd = new DefaultHelp<T>();
		this.defaultHelpCmd.setHandler(this);
	}
	public CommandHandler(T self, String prefix, String ownerID) {
		this(self, prefix, ownerID, null);
	}
	public CommandHandler(T self, String prefix) {
		this(self, prefix, null);
	}
	public CommandHandler(T self) {
		this(self, null);
	}
	public void registerCommand(Command<T> command) {
		command.setHandler(this);
		commands.put(command.getName(), command);
		for(String alias : command.getAliases()) {
			commands.put(alias, command);
		}
		System.out.println("Registered " + (command.hide() ? "private " : "") + "command " + command.getName());
	}
	public void setRatelimit(int limit, TimeUnit unit) {
		if(unit == null || limit <= 0) {
			this.limit = 0;
			this.unit = null;
			return;
		}
		this.limit = limit;
		this.unit = unit;
		if(limitService == null) {
			this.limitService = Executors.newScheduledThreadPool(10);
			this.ratelimits = Collections.synchronizedMap(new HashMap<>());
		}
	}
	public List<Command<T>> getRegisteredCommands() {
		return commands.values().stream().distinct().collect(Collectors.toList());
	}
	public boolean isMentionPrefix() {
		return mentionPrefix;
	}
	public String getPrefix() {
		if(mentionPrefix && jda != null) {
			return "@"+jda.getSelfUser().getName()+" ";
		}
		return prefix;
	}
	public JDA getJDA() {
		return jda;
	}
	public T getSelf() {
		return self;
	}
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(jda == null) {
			jda = event.getJDA();
		}
		if(!event.getChannel().canTalk()) {
			return;
		}
		String msg = event.getMessage().getContentRaw().toLowerCase();
		if(mentionPrefix) {
			if(prefix == null) {
				prefix = jda.getSelfUser().getId();
			}
			msg = msg.replaceFirst("<@!?"+prefix+">", prefix);
		}
		if(event.getAuthor().isBot() || !msg.startsWith(prefix)) {
			return;
		}
		String cmd = msg.substring(prefix.length()).trim().toLowerCase();
		if(cmd.contains(" ")){
			cmd = cmd.split(" ")[0];
		}
		Command<T> command = commands.get(cmd);
		if(command == null && cmd.equals("help")) {
			command = defaultHelpCmd;
		}
		if(command != null) {
			if(unit != null) {
				final String id = event.getAuthor().getId();
				boolean limited = false;
				if(ratelimits.containsKey(id)) {
					ratelimits.get(id).cancel(true);
					event.getChannel().sendMessage("You are being ratelimited! The ratelimit is set to "+limit+" "+unit.toString().toLowerCase()).queue();
					limited = true;
				}
				ratelimits.put(id, limitService.schedule(() -> {
					ratelimits.remove(id);
				}, limit, unit));
				
				if(limited) {
					return;
				}
			}
			try {
				command.handle(event, self);
			}
			catch(Exception e) {
				event.getChannel().sendMessage("Oh no! Something went wrong while executing that command!\nThis incident has been reported.\n" + e).queue();
				if(this.owner == null) {
					this.owner = jda.retrieveApplicationInfo().complete().getOwner().getId();
				}
				User owner = jda.getUserById(this.owner);
				if(owner != null) {
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
