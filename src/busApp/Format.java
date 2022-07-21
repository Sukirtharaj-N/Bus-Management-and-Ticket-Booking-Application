package busApp;

public class Format {
	String condition=null;
	Object value=null;
	
	Format(String condition,Object value) {
		this.condition=condition;
		this.value=value;
	}
	
    public String getCondition() {
    	return this.condition;
    }
    
    public Object getValue() {
    	return this.value;
    }
	
}


