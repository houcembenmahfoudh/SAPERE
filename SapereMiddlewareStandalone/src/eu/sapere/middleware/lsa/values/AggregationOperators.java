package eu.sapere.middleware.lsa.values;

/**
 * Enumerates supported Aggregation Operators
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public enum AggregationOperators {

	/**
	 * Max operator.
	 */
	MAX("max"),

	/**
	 * Min operator.
	 */
	MIN("min"),

	/**
	 * Avg operator.
	 */
	AVG("avg");

	private String text;

	AggregationOperators(String text) {
		this.text = text;
	}

	/**
	 * Returns the textual representation of this Aggregation Operator
	 * 
	 * @return the textual representation of this Aggregation Operator
	 */
	public String getText() {
		return this.text;
	}

	// public static Id max (Vector<Lsa> allLsa){
	//
	// Id ret = null;
	//
	// if(allLsa.isEmpty())
	// return null;
	//
	// int max = new
	// Integer(allLsa.elementAt(0).getProperty(allLsa.elementAt(0).getFieldValue()).getValue().elementAt(0)).intValue();
	// ret = allLsa.elementAt(0).getId();
	//
	// for(int i=0; i<allLsa.size(); i++){
	// if (new
	// Integer(allLsa.elementAt(i).getProperty(allLsa.elementAt(0).getFieldValue()).getValue().elementAt(0)).intValue()
	// > max){
	// max = new
	// Integer(allLsa.elementAt(i).getProperty(allLsa.elementAt(0).getFieldValue()).getValue().elementAt(0)).intValue();
	// ret = allLsa.elementAt(i).getId();
	// }
	// }
	//
	// return ret;
	//
	// }
	//
	// public static Id min (Vector<Lsa> allLsa){
	//
	// Id ret = null;
	//
	// if(allLsa.isEmpty())
	// return null;
	//
	// int min = new
	// Integer(allLsa.elementAt(0).getProperty(allLsa.elementAt(0).getFieldValue()).getValue().elementAt(0)).intValue();
	// ret = allLsa.elementAt(0).getId();
	//
	// for(int i=0; i<allLsa.size(); i++){
	// if (new
	// Integer(allLsa.elementAt(i).getProperty(allLsa.elementAt(0).getFieldValue()).getValue().elementAt(0)).intValue()
	// < min){
	// min = new
	// Integer(allLsa.elementAt(i).getProperty(allLsa.elementAt(0).getFieldValue()).getValue().elementAt(0)).intValue();
	// ret = allLsa.elementAt(i).getId();
	// }
	// }
	//
	// return ret;
	//
	// }
	//
	// public static Lsa mean (Vector<Lsa> allLsa){
	//
	// Lsa ret = null;
	//
	// if(allLsa.isEmpty())
	// return null;
	//
	// int val = 0;
	//
	// for(int i=0; i<allLsa.size(); i++){
	// val += new
	// Integer(allLsa.elementAt(i).getProperty(allLsa.elementAt(0).getFieldValue()).getValue().elementAt(0)).intValue();
	// }
	//
	//
	// val = val / allLsa.size();
	//
	// ret = allLsa.elementAt(0).getCopy();
	// try {
	// ret.setProperty(new Property(allLsa.elementAt(0).getFieldValue(),
	// ""+val));
	// } catch (UnresolvedPropertyNameException e) {
	// e.printStackTrace();
	// }
	//
	// ret.addProperty(new
	// SyntheticProperty(SyntheticPropertyName.LAST_MODIFIED, new String(""+new
	// Date().getTime())));
	//
	// return ret;
	//
	// }

}
