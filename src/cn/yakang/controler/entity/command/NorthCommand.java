package cn.yakang.controler.entity.command;

public class NorthCommand extends ICommand {
	private static NorthCommand command;
	private NorthCommand(){
		actionId = NORTH;
	}
	
	public static NorthCommand getInstance(){
		if(command == null){
			command = new NorthCommand();
		}
		return command;
	}
}
