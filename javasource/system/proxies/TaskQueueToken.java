// This file was generated by Mendix Studio Pro.
//
// WARNING: Code you write here will be lost the next time you deploy the project.

package system.proxies;

/**
 * Represents a concurrency token to limit to parallely executing tasks in whole cluster when cluster wide limit is enabled for a Queue.
 */
public class TaskQueueToken
{
	private final com.mendix.systemwideinterfaces.core.IMendixObject taskQueueTokenMendixObject;

	private final com.mendix.systemwideinterfaces.core.IContext context;

	/**
	 * Internal name of this entity
	 */
	public static final java.lang.String entityName = "System.TaskQueueToken";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		QueueName("QueueName"),
		XASId("XASId"),
		ValidUntil("ValidUntil");

		private final java.lang.String metaName;

		MemberNames(java.lang.String s)
		{
			metaName = s;
		}

		@java.lang.Override
		public java.lang.String toString()
		{
			return metaName;
		}
	}

	public TaskQueueToken(com.mendix.systemwideinterfaces.core.IContext context)
	{
		this(context, com.mendix.core.Core.instantiate(context, entityName));
	}

	protected TaskQueueToken(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixObject taskQueueTokenMendixObject)
	{
		if (taskQueueTokenMendixObject == null) {
			throw new java.lang.IllegalArgumentException("The given object cannot be null.");
		}
		if (!com.mendix.core.Core.isSubClassOf(entityName, taskQueueTokenMendixObject.getType())) {
			throw new java.lang.IllegalArgumentException(String.format("The given object is not a %s", entityName));
		}	

		this.taskQueueTokenMendixObject = taskQueueTokenMendixObject;
		this.context = context;
	}

	/**
	 * @deprecated Use 'TaskQueueToken.load(IContext, IMendixIdentifier)' instead.
	 */
	@java.lang.Deprecated
	public static system.proxies.TaskQueueToken initialize(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixIdentifier mendixIdentifier) throws com.mendix.core.CoreException
	{
		return system.proxies.TaskQueueToken.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.createSudoClone() can be used to obtain sudo access).
	 * @param context The context to be used
	 * @param mendixObject The Mendix object for the new instance
	 * @return a new instance of this proxy class
	 */
	public static system.proxies.TaskQueueToken initialize(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixObject mendixObject)
	{
		return new system.proxies.TaskQueueToken(context, mendixObject);
	}

	public static system.proxies.TaskQueueToken load(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixIdentifier mendixIdentifier) throws com.mendix.core.CoreException
	{
		com.mendix.systemwideinterfaces.core.IMendixObject mendixObject = com.mendix.core.Core.retrieveId(context, mendixIdentifier);
		return system.proxies.TaskQueueToken.initialize(context, mendixObject);
	}

	public static java.util.List<system.proxies.TaskQueueToken> load(com.mendix.systemwideinterfaces.core.IContext context, java.lang.String xpathConstraint) throws com.mendix.core.CoreException
	{
		return com.mendix.core.Core.createXPathQuery(String.format("//%1$s%2$s", entityName, xpathConstraint))
			.execute(context)
			.stream()
			.map(obj -> system.proxies.TaskQueueToken.initialize(context, obj))
			.collect(java.util.stream.Collectors.toList());
	}

	/**
	 * Commit the changes made on this proxy object.
	 * @throws com.mendix.core.CoreException
	 */
	public final void commit() throws com.mendix.core.CoreException
	{
		com.mendix.core.Core.commit(context, getMendixObject());
	}

	/**
	 * Commit the changes made on this proxy object using the specified context.
	 * @throws com.mendix.core.CoreException
	 */
	public final void commit(com.mendix.systemwideinterfaces.core.IContext context) throws com.mendix.core.CoreException
	{
		com.mendix.core.Core.commit(context, getMendixObject());
	}

	/**
	 * Delete the object.
	 */
	public final void delete()
	{
		com.mendix.core.Core.delete(context, getMendixObject());
	}

	/**
	 * Delete the object using the specified context.
	 */
	public final void delete(com.mendix.systemwideinterfaces.core.IContext context)
	{
		com.mendix.core.Core.delete(context, getMendixObject());
	}
	/**
	 * @return value of QueueName
	 */
	public final java.lang.String getQueueName()
	{
		return getQueueName(getContext());
	}

	/**
	 * @param context
	 * @return value of QueueName
	 */
	public final java.lang.String getQueueName(com.mendix.systemwideinterfaces.core.IContext context)
	{
		return (java.lang.String) getMendixObject().getValue(context, MemberNames.QueueName.toString());
	}

	/**
	 * Set value of QueueName
	 * @param queuename
	 */
	public final void setQueueName(java.lang.String queuename)
	{
		setQueueName(getContext(), queuename);
	}

	/**
	 * Set value of QueueName
	 * @param context
	 * @param queuename
	 */
	public final void setQueueName(com.mendix.systemwideinterfaces.core.IContext context, java.lang.String queuename)
	{
		getMendixObject().setValue(context, MemberNames.QueueName.toString(), queuename);
	}

	/**
	 * @return value of XASId
	 */
	public final java.lang.String getXASId()
	{
		return getXASId(getContext());
	}

	/**
	 * @param context
	 * @return value of XASId
	 */
	public final java.lang.String getXASId(com.mendix.systemwideinterfaces.core.IContext context)
	{
		return (java.lang.String) getMendixObject().getValue(context, MemberNames.XASId.toString());
	}

	/**
	 * Set value of XASId
	 * @param xasid
	 */
	public final void setXASId(java.lang.String xasid)
	{
		setXASId(getContext(), xasid);
	}

	/**
	 * Set value of XASId
	 * @param context
	 * @param xasid
	 */
	public final void setXASId(com.mendix.systemwideinterfaces.core.IContext context, java.lang.String xasid)
	{
		getMendixObject().setValue(context, MemberNames.XASId.toString(), xasid);
	}

	/**
	 * @return value of ValidUntil
	 */
	public final java.util.Date getValidUntil()
	{
		return getValidUntil(getContext());
	}

	/**
	 * @param context
	 * @return value of ValidUntil
	 */
	public final java.util.Date getValidUntil(com.mendix.systemwideinterfaces.core.IContext context)
	{
		return (java.util.Date) getMendixObject().getValue(context, MemberNames.ValidUntil.toString());
	}

	/**
	 * Set value of ValidUntil
	 * @param validuntil
	 */
	public final void setValidUntil(java.util.Date validuntil)
	{
		setValidUntil(getContext(), validuntil);
	}

	/**
	 * Set value of ValidUntil
	 * @param context
	 * @param validuntil
	 */
	public final void setValidUntil(com.mendix.systemwideinterfaces.core.IContext context, java.util.Date validuntil)
	{
		getMendixObject().setValue(context, MemberNames.ValidUntil.toString(), validuntil);
	}

	/**
	 * @return the IMendixObject instance of this proxy for use in the Core interface.
	 */
	public final com.mendix.systemwideinterfaces.core.IMendixObject getMendixObject()
	{
		return taskQueueTokenMendixObject;
	}

	/**
	 * @return the IContext instance of this proxy, or null if no IContext instance was specified at initialization.
	 */
	public final com.mendix.systemwideinterfaces.core.IContext getContext()
	{
		return context;
	}

	@java.lang.Override
	public boolean equals(Object obj)
	{
		if (obj == this) {
			return true;
		}
		if (obj != null && getClass().equals(obj.getClass()))
		{
			final system.proxies.TaskQueueToken that = (system.proxies.TaskQueueToken) obj;
			return getMendixObject().equals(that.getMendixObject());
		}
		return false;
	}

	@java.lang.Override
	public int hashCode()
	{
		return getMendixObject().hashCode();
	}

	/**
	 * @return String name of this class
	 */
	public static java.lang.String getType()
	{
		return entityName;
	}

	/**
	 * @return String GUID from this object, format: ID_0000000000
	 * @deprecated Use getMendixObject().getId().toLong() to get a unique identifier for this object.
	 */
	@java.lang.Deprecated
	public java.lang.String getGUID()
	{
		return "ID_" + getMendixObject().getId().toLong();
	}
}
