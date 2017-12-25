package com.mywallet.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mywallet.config.MyWalletConfig;
import com.mywallet.domain.Action;
import com.mywallet.repository.ActionRepository;
import com.mywallet.util.MyWalletUtilities;

@Service
public class ActionService {

	private final static Logger logger = Logger.getLogger(ActionService.class);

	@Autowired
	private MyWalletConfig myWalletConfig;

	@Autowired
	private ActionRepository actionRepository;

	public ActionService(){
		logger.info("ActionService class Bean is created : ");
	}

	@PostConstruct
	public void init() {
		logger.info("initilizing action");
		intializeActions(myWalletConfig.getControllerPackages());
	}

	public void intializeActions(String[] packageNames){
		//these action object derived from @apiAction annotation 
		ArrayList<Action> actions = MyWalletUtilities.getAllActionObject(packageNames);
		logger.info("Action Objects read from @ApiAction annotation : "+actions.size());
		List<Action> dbActionArray = getAllAction();
		if(dbActionArray == null)
			return;
		logger.info("Action Objects array from database "+dbActionArray.size());
		
		List<Action> uniqueActionArray =  MyWalletUtilities.getListA_Minus_ListBObjects(actions, dbActionArray, "handlerMethodName");
		logger.info(uniqueActionArray.size()+" new action are created for insert into database.");
		for(Action activity : uniqueActionArray){
			save(activity);
		}
		logger.info("action inserted into database Sucessfullly");
	}

	public Action save(Action action){
		logger.info("inside save method of action service :");
		try {
			return actionRepository.save(action);
		} catch (Exception exception) {
			logger.error("save Action object on database "+exception);
			return null;
		}
	}

	public List<Action> persist(){
		logger.info("inside persist method of action service :");
		try {
			return actionRepository.findAll();
		} catch (Exception e) {
			logger.error("fetch action object from database :"+e);
			return null;
		}
	}

	public Action findByActionId(Long actionId){
		logger.info("inside findByActionId method of action service :");
		try {
			return actionRepository.findByActionId(actionId);
		} catch (Exception e) {
			logger.error("fetching action object by actionId from database :"+e);
			return null;
		}

	}

	public Action findByHandlerMethodName(String handlerMethodName){
		logger.info("inside findByHandlerMethodName method of action service :");
		try {
			return actionRepository.findByHandlerMethodName(handlerMethodName);
		} catch (Exception e) {
			logger.error("fetching action object by handlerMethodName from database :"+e);
			return null;
		}
	}

	public List<Action> getAllAction(){
		logger.debug("returing all activities list");
		try{
			return actionRepository.findAll();
		}catch(Exception e){
			logger.debug("Exception occure while fetch all activities from database : "+e);
			return null;			
		}
	}
}
