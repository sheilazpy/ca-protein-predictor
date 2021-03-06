package casspserver.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("predictCASSPService")
public interface PredictCASSPService extends RemoteService {
	String predict(String aaSeq);
}
