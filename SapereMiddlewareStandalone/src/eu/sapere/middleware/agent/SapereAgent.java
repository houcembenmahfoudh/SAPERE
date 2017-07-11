package eu.sapere.middleware.agent;

import java.util.UUID;

import eu.sapere.middleware.lsa.Lsa;
import eu.sapere.middleware.lsa.Property;
import eu.sapere.middleware.lsa.autoupdate.EventGenerator;
import eu.sapere.middleware.lsa.autoupdate.PropertyValueEvent;
import eu.sapere.middleware.lsa.autoupdate.PropertyValueListener;
import eu.sapere.middleware.lsa.exception.UnresolvedPropertyNameException;
import eu.sapere.middleware.lsa.values.AggregationOperators;
import eu.sapere.middleware.lsa.values.PropertyName;
import eu.sapere.middleware.node.NodeManager;

/**
 * The abstract class that realize an agent that manages an LSA. The Agent is
 * represented by an implicit LSA, each operation on the LSA is automatically
 * and transparently propagated to the LSA space.
 * 
 * @author Gabriella Castelli (UNIMORE)
 * 
 */
public abstract class SapereAgent extends LsaAgent implements PropertyValueListener {

	/**
	 * Instantiates the Sapere Agent
	 * 
	 * @param name
	 *            The name of the Agent
	 */
	public SapereAgent(String name) {
		super(name);
	}

	/**
	 * Use this method to set the initial content of the LSA managed by this
	 * SapereAgent.
	 */
	public abstract void setInitialLSA();

	/**
	 * Adds Properties to the LSA managed by this SapereAgent
	 * 
	 * @param properties
	 *            The Properties to be added
	 */
	public void addProperty(Property... properties) {

		Property p[] = properties;
		for (int i = 0; i < p.length; i++) {
			lsa.addProperty(p[i]);
		}
		submitOperation();

	}

	/**
	 * Adds Property to the LSA managed by this SapereAgent
	 * 
	 * @param p
	 *            The Property to be added
	 */
	public void addProperty(Property p) {
		lsa.addProperty(p);
		// submitOperation();
	}

	/**
	 * Adds the Properties that trigger a direct propagation to a specific node
	 * 
	 * @param destinationNode
	 *            The name of the destination node
	 */
	public void addDirectPropagation(String destinationNode) {

		// this.removeProperty(PropertyName.DIFFUSION_OP.toString());
		// this.removeProperty(PropertyName.DESTINATION.toString());

		this.addProperty(new Property(PropertyName.DIFFUSION_OP.toString(), "direct"),
				new Property(PropertyName.DESTINATION.toString(), destinationNode),
				new Property(PropertyName.PREVIOUS.toString(), "local"),
				new Property(PropertyName.UUID.toString(), UUID.randomUUID().toString()));

	}

	/**
	 * Adds the Properties that trigger a direct propagation to all neighbor
	 * nodes
	 */
	public void addDirectPropagation() {
		this.addProperty(new Property(PropertyName.DIFFUSION_OP.toString(), "direct"),
				new Property(PropertyName.DESTINATION.toString(), "all"),
				new Property(PropertyName.PREVIOUS.toString(), "local"),
				new Property(PropertyName.UUID.toString(), UUID.randomUUID().toString()));

	}

	/**
	 * Adds the Properties that trigger a gradient propagation
	 * 
	 * @param nHop
	 *            The number of hops
	 * @param aggregationOperator
	 *            The aggregation operator to be used to aggregate gradient LSAs
	 * @param gradientFieldName
	 *            The name of the gradient field
	 */
	public void addGradient(int nHop, String aggregationOperator, String gradientFieldName) {
		addProperty(new Property(PropertyName.DIFFUSION_OP.toString(), "GRADIENT_" + nHop),
				new Property(PropertyName.AGGREGATION_OP.toString(), aggregationOperator),
				new Property(PropertyName.FIELD_VALUE.toString(), gradientFieldName),
				new Property(PropertyName.SOURCE.toString(), NodeManager.getSpaceName()),
				new Property(PropertyName.PREVIOUS.toString(), "local"),
				new Property(PropertyName.DESTINATION.toString(), "default"),
				new Property(gradientFieldName, "" + nHop));
		addProperty(new Property("uuid", UUID.randomUUID().toString()));
	}

	/**
	 * @param gradientId
	 * @param maxHop
	 */
	public void addDynamicGradient(String gradientId, int maxHop) {
		lsa.addProperty(new Property("agent-name", this.agentName));
		// lsa.addProperty(new Property("name", gradientId));
		// lsa.addProperty(new Property("gradient", gradientId));
		lsa.addProperty(new Property(PropertyName.DIFFUSION_OP.toString(), "GRADIENT_" + maxHop));
		// lsa.addProperty(new Property("uuid", UUID.randomUUID().toString()));
		// lsa.addProperty(new Property("spread", "true"));
		lsa.addProperty(new Property(PropertyName.FIELD_VALUE.toString(), gradientId));
		lsa.addProperty(new Property(PropertyName.SOURCE.toString(), NodeManager.getSpaceName()));
		lsa.addProperty(new Property(PropertyName.PREVIOUS.toString(), "local"));
		// default gradient max hop
		lsa.addProperty(new Property(PropertyName.GRADIENT_MAX_HOP.toString(), "" + maxHop));

		lsa.addSubDescription("q", new Property("data-val	ue", "datum"));
		// lsa.addProperty(new Property("decay", "" + maxHop));
		addProperty(new Property("uuid", UUID.randomUUID().toString()));

	}

	/**
	 * Adds the Properties that trigger an aggregation
	 * 
	 * @param fieldName
	 *            The name of the field to be aggregated
	 * @param AggregationOperator
	 *            The aggregation operator
	 */
	public void addOtherAggregation(String fieldName, String AggregationOperator) {
		addProperty(new Property(PropertyName.AGGREGATION_OP.toString(), AggregationOperator));
		addProperty(new Property(PropertyName.FIELD_VALUE.toString(), fieldName));
	}

	/**
	 * Adds the Properties that trigger an aggregation
	 * 
	 * @param fieldName
	 *            The name of the field to be aggregated
	 * @param AggregationOperator
	 *            The aggregation operator
	 * @param aggregationSource
	 *            The source property for LSAs to be aggregated together
	 */
	public void addSelfAggregation(String fieldName, String AggregationOperator, String aggregationSource) {
		addProperty(new Property(PropertyName.AGGREGATION_OP.toString(), AggregationOperator));
		addProperty(new Property(PropertyName.FIELD_VALUE.toString(), fieldName));
		addProperty(new Property(PropertyName.SOURCE.toString(), aggregationSource));
	}

	/**
	 * Adds the Properties that trigger an aggregation to get the newest LSA
	 * 
	 * @param aggregationSource
	 *            The source property for LSAs to be aggregated together
	 */
	public void addSelfAggregationNEWEST(String aggregationSource) {
		addProperty(new Property(PropertyName.AGGREGATION_OP.toString(), AggregationOperators.MAX.toString()));
		addProperty(new Property(PropertyName.FIELD_VALUE.toString(), "#lastModified"));
		addProperty(new Property(PropertyName.SOURCE.toString(), aggregationSource));
	}

	/**
	 * Adds the Properties that trigger an aggregation to get the oldest LSA
	 * 
	 * @param aggregationSource
	 *            The source property for LSAs to be aggregated together
	 */
	public void addSelfAggregationOLDEST(String aggregationSource) {
		addProperty(new Property(PropertyName.AGGREGATION_OP.toString(), AggregationOperators.MIN.toString()));
		addProperty(new Property(PropertyName.FIELD_VALUE.toString(), "#lastModified"));
		addProperty(new Property(PropertyName.SOURCE.toString(), aggregationSource));
	}

	/**
	 * Adds a decay property
	 * 
	 * @param decayValue
	 *            The initial decay value
	 */
	public void addDecay(int decayValue) {
		addProperty(new Property(PropertyName.DECAY.toString(), "" + decayValue));
	}

	/**
	 * Adds a Property that automatically updates
	 * 
	 * @param name
	 *            The name of the Property
	 * @param eg
	 *            The generator of automatically updates
	 */
	public void addProperty(String name, EventGenerator eg) {
		eg.setPropertyName(name);
		eg.addPropertyValueListener(this);
	}

	/**
	 * Sets the value of an existing Property
	 * 
	 * @param p
	 *            The new Property value
	 */
	public void setProperty(Property p) {

		try {
			lsa.setProperty(p);
			submitOperation();
		} catch (UnresolvedPropertyNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Removes a Property from the managed LSA
	 * 
	 * @param name
	 */
	public void removeProperty(String name) {
		lsa.removeProperty(name);
		submitOperation();
	}

	/**
	 * Adds a SubDescription with a single Property
	 * 
	 * @param name
	 * @param value
	 */
	public void addSubDescription(String name, Property value) {
		lsa.addSubDescription(name, value);
		submitOperation();
	}

	/**
	 * Adds a SubDescription with a variable number of Properties
	 * 
	 * @param name
	 * @param values
	 */
	public void addSubDescription(String name, Property... values) {
		// SubDescription s = null;
		lsa.addSubDescription(name, values);

		// s.toString();

		submitOperation();
	}

	/**
	 * Removes the specified SubDescription from the managed LSA
	 * 
	 * @param name
	 */
	public void removeSubDescription(String name) {
		lsa.removeSubDescription(name);
		submitOperation();
	}

	/**
	 * Stes the value of an existing SubDescription
	 * 
	 * @param name
	 * @param value
	 */
	public void setSubDescription(String name, Property value) {
		lsa.setSubDescription(name, value);
		submitOperation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void propertyValueGenerated(PropertyValueEvent event) {
		if (!((Lsa) lsa).hasProperty(event.getPropertyName())) {
			addProperty(new Property(event.getPropertyName(), event.getValue()));
		} else {
			// update
			setProperty(new Property(event.getPropertyName(), event.getValue()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void propertyValueAppendGenerated(PropertyValueEvent event) {
		if (!((Lsa) lsa).hasProperty(event.getPropertyName())) {
			addProperty(new Property(event.getPropertyName(), event.getValue()));
		} else {
			// update
			// setProperty(new Property(event.getPropertyName(),
			// event.getValue()));
			Property p = lsa.getProperty(event.getPropertyName());
			p.getPropertyValue().addValue(event.getValue());
			setProperty(new Property(event.getPropertyName(), p.getValue()));

		}

	}

}
