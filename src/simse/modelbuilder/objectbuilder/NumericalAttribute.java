/* This is a class for attributes that are numerical types, such as integers and doubles. */

package simse.modelbuilder.objectbuilder;

public class NumericalAttribute extends Attribute
{
	Number min; // minimum required value of this attribute
	Number max; // maximum possible value of this attribute
	Integer minNumFractionDigits; // min number of digits to show after the decimal points for this attribute's value
	Integer maxNumFractionDigits; // max number of digits to show after the decimal points for this attriubte's value


	public NumericalAttribute(String name, int type, boolean visible, boolean key, boolean visibleOnCompletion,	Number minimum, 
		Number maximum, Integer minNumDigits, Integer maxNumDigits)
	{
		super(name, type, visible, key, visibleOnCompletion);

		minNumFractionDigits = minNumDigits;
		maxNumFractionDigits = maxNumDigits;
		
		if(type == AttributeTypes.INTEGER)
		{
			if(minimum != null)
			{
				min = new Integer(minimum.intValue());
			}
			else
			{
				min = null;
			}
			if(maximum != null)
			{
				max = new Integer(maximum.intValue());
			}
			else
			{
				max = null;
			}
		}
		else if(type == AttributeTypes.DOUBLE)
		{
			if(minimum != null)
			{
				min = new Double(minimum.doubleValue());
			}
			else
			{
				min = null;
			}
			if(maximum != null)
			{
				max = new Double(maximum.doubleValue());
			}
			else
			{
				max = null;
			}
		}
	}


	public NumericalAttribute(NonNumericalAttribute n, int newType) // casts n into a new numerical attribute with null min and max values,
		// min and max num fraction digits = null, and type newType
	{
		super(n.getName(), newType, n.isVisible(), n.isKey(), n.isVisibleOnCompletion());
		minNumFractionDigits = null;
		maxNumFractionDigits = null;
		min = null;
		max = null;
	}
	
	
	public Integer getMinNumFractionDigits()
	{
		return minNumFractionDigits;
	}
	
	
	public Integer getMaxNumFractionDigits()
	{
		return maxNumFractionDigits;
	}	
	
	
	public void setMinNumFractionDigits(Integer newNum)
	{
		minNumFractionDigits = newNum;
	}
	
	
	public void setMaxNumFractionDigits(Integer newNum)
	{
		maxNumFractionDigits = newNum;
	}	


	public Number getMinValue()
	{
		return min;
	}


	public Number getMaxValue()
	{
		return max;
	}


	public void setMinValue(Integer minimum)
	{
		min = new Integer(minimum.intValue());
	}


	public void setMinValue(Double minimum)
	{
		min = new Double(minimum.doubleValue());
	}



	public void setMaxValue(Integer maximum)
	{
		max = new Integer(maximum.intValue());
	}


	public void setMaxValue(Double maximum)
	{
		max = new Double(maximum.doubleValue());
	}


	public void setMinBoundless() //  sets the min value to boundless
	{
		min = null;
	}


	public void setMaxBoundless() // sets the max value to boundless
	{
		max = null;
	}


	public boolean isMinBoundless() // returns true if the min value is boundless, false otherwise
	{
		if(min == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public boolean isMaxBoundless() // returns true if the max value is boundless, false otherwise
	{
		if(max == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
