package entities;

import java.util.ArrayList;
import java.util.List;

public final class ActionLog {
	static List<List<IDeviceAction>> actionsByCommandIndex = new ArrayList<List<IDeviceAction>>();

	public static void resetLog(){
		actionsByCommandIndex = new ArrayList<List<IDeviceAction>>();
		nextCommand();
	}
	
	/**
	 * Adding new action to the current step
	 * 
	 * @param action
	 */
	public static void addAction(IDeviceAction action){
		actionsByCommandIndex.get(actionsByCommandIndex.size() - 1).add(action);
	}
	
	/**
	 * Called after each line in a trace, moves to the next step of action log
	 * 
	 * NOTE: May be important to add sliding window so the list will have limited size 
	 */
	public static void nextCommand(){
		actionsByCommandIndex.add(new ArrayList<IDeviceAction>());
	}
	
	public static List<IDeviceAction> getActionsByType(Class<?> actionClass, int commandIndex){
		List<IDeviceAction> result = new ArrayList<IDeviceAction>();
		for (IDeviceAction action : actionsByCommandIndex.get(commandIndex)) {
			if (actionClass.isInstance(action)){
				result.add(action);
			}
		} 
		return result;
	}
	
	public static List<IDeviceAction> getActionsByType(Class<?> actionClass){
		return getActionsByType(actionClass, actionsByCommandIndex.size() - 1);
	}
	
}
