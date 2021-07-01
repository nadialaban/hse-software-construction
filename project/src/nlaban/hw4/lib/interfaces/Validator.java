package nlaban.hw4.lib.interfaces;

import java.util.Set;

public interface Validator {

	Set<ValidationError> validate(Object object);

}
