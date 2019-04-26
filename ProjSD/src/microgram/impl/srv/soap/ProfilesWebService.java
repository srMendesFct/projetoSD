package microgram.impl.srv.soap;


import microgram.api.Profile;
import microgram.api.java.Profiles;
import microgram.api.soap.MicrogramException;
import microgram.api.soap.SoapProfiles;
import microgram.impl.srv.java.JavaProfiles;

//Make this class concrete.
public abstract class ProfilesWebService extends SoapService implements SoapProfiles {

	final Profiles impl;
	
	protected ProfilesWebService() {
		this.impl = new JavaProfiles();
	}
	
	@Override
	public Profile getProfile( String userId ) throws MicrogramException {
		return super.resultOrThrow( impl.getProfile(userId));
	}
	
}
