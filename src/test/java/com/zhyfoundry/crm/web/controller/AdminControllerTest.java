package com.zhyfoundry.crm.web.controller;

import java.io.IOException;
import java.util.Collections;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.MapBindingResult;

import com.zhyfoundry.crm.DBMaker;
import com.zhyfoundry.crm.TestBase;
import com.zhyfoundry.crm.entity.Administrator;

public class AdminControllerTest extends TestBase {

	@Autowired
	protected AdminController controller;

	@Override
	public void execute() throws Exception {
		Assert.assertEquals(AdminController.ADMIN_INDEX,//
				controller.login(new Administrator(DBMaker.USERNAME_ADMIN,
						DBMaker.PASSWORD_ADMIN), new MapBindingResult(
						Collections.emptyMap(), "admin"), request, response));
	}
}