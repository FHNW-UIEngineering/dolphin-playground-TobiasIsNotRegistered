package myapp.servlet;

import myapp.service.impl.MyRemoteService;
import org.opendolphin.core.server.DefaultServerDolphin;
import org.opendolphin.server.adapter.DolphinServlet;

import myapp.controller.Reception;

public class MyAppServlet extends DolphinServlet {

	@Override
	protected void registerApplicationActions(DefaultServerDolphin serverDolphin) {
		serverDolphin.register(new Reception(new MyRemoteService()));
	}
}
