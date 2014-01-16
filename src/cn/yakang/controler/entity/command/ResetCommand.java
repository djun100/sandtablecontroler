package cn.yakang.controler.entity.command;

public class ResetCommand extends ICommand {
	private static ResetCommand command;
	private ResetCommand(){
		actionId = RESET;
	}
	public static ResetCommand getInstance(){
		if(command == null){
			command = new ResetCommand();
		}
		return command;
	}
}
