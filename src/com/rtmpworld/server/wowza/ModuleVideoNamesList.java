package com.rtmpworld.server.wowza;

import com.wowza.wms.application.*;
import com.wowza.wms.module.*;
import com.wowza.wms.logging.WMSLogger;

public class ModuleVideoNamesList extends ModuleBase {
	
	public static String MODULE_NAME = "ModuleVideoNamesList";
	
	private WMSLogger logger;
	
	protected static boolean moduleDebug;


	public void onAppCreate(IApplicationInstance appInstance)
	{
		this.logger = getLogger();
		
		if(moduleDebug){
			this.logger.info(MODULE_NAME + ".onAppCreate");
		}
		
		this.readProperties();
	}
	
	
	
	private void readProperties()
	{
		if(moduleDebug){
			this.logger.info(MODULE_NAME + ".readProperties => reading properties");
		}
		
	}

}
