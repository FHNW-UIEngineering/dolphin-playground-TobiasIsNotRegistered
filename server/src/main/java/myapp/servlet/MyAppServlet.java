package myapp.servlet;

import myapp.MyRemoteService;
import org.opendolphin.core.server.DefaultServerDolphin;
import org.opendolphin.server.adapter.DolphinServlet;

import myapp.Reception;

public class MyAppServlet extends DolphinServlet {

	@Override
	protected void registerApplicationActions(DefaultServerDolphin serverDolphin) {
		serverDolphin.register(new Reception(new MyRemoteService()));
	}
}
