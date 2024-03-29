package net.benjaminurquhart.jch;

public abstract class AbstractCommand<T> {

	private String name;
	private String[] args;

	private CommandHandler<T> handler;
	
	public AbstractCommand(){
		this(null);
	}
	public AbstractCommand(String name, String... args){
		if(name == null){
			name = this.getClass().getSimpleName();
			
			if(!this.isMessageInteraction()) {
				name = name.toLowerCase();
			}
		}
		this.name = name;
		this.args = args;
		
		if(this.isSlashCommand() && this.isMessageInteraction()) {
			throw new IllegalStateException("Cannot be both a slash command and a message interaction.");
		}
	}
	public String getHelpMenu(){
		return Usage.getUsage(handler, this, args);
	}
	public String getName(){
		return this.name;
	}
	public String getDescription(){
		return "no description provided";
	}
	public boolean hide(){
		return false;
	}
	public String[] getAliases() {
		return new String[0];
	}
	protected void setHandler(CommandHandler<T> handler){
		this.handler = handler;
	}
	public CommandHandler<T> getHandler(){
		return handler;
	}
	public boolean isSlashCommand() {
		return this.getClass().isAnnotationPresent(SlashCommand.class);
	}
	public boolean isMessageInteraction() {
		return this.getClass().isAnnotationPresent(MessageInteraction.class);
	}
	public boolean isUsableInMessage() {
		if(isMessageInteraction()) {
			return false;
		}
		
		SlashCommand annotation = this.getClass().getAnnotation(SlashCommand.class);
		
		return annotation == null || annotation.value();
	}
	public abstract void handle(CommandEvent event, T self);
}
