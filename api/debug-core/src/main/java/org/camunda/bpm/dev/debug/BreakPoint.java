package org.camunda.bpm.dev.debug;

import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.runtime.AtomicOperation;

import static org.camunda.bpm.dev.debug.BreakPointSpecs.*;

/**
 * @author Daniel Meyer
 *
 */
public class BreakPoint {

  /**
   * the Id of the breakpoint
   */
  protected String id;

  /**
   * The {@link BreakPointSpec}
   */
  protected BreakPointSpec breakPointSpec;

  /**
   * The id of the activity to break on
   */
  protected String activityId;

  /**
   * The id of the transition to break on
   */
  protected String transitionId;

  /**
   * The id of the process definition to break on
   */
  protected String processDefinitionId;

  protected String processInstanceId;

  protected BreakPointCondition condition;

  public BreakPoint(BreakPointSpec breakPointSpec, String processDefinitionId, String activityId, BreakPointCondition condition) {
    this.breakPointSpec = breakPointSpec;
    this.activityId = activityId;
    this.processDefinitionId = processDefinitionId;
    this.condition = condition;
  }

  public boolean breakOnOperation(AtomicOperation operation, ExecutionEntity execution) {
    boolean shouldBreak = true;

    shouldBreak &= processDefinitionId.equals(execution.getProcessDefinitionId());

    if(AT_TRANSITION.equals(breakPointSpec)) {
      shouldBreak &= transitionId.equals(execution.getCurrentTransitionId());
    } else {
      shouldBreak &= activityId.equals(execution.getActivityId());
    }

    shouldBreak &= breakPointSpec.breakOnOperation(operation);

    if (shouldBreak && condition != null) {
      shouldBreak &= condition.evaluate(execution);
    }

    return shouldBreak;
  }

  public String getActivityId() {
    return activityId;
  }

  public String getProcessDefinitionId() {
    return processDefinitionId;
  }

  public String getTransitionId() {
    return transitionId;
  }

  public String getId() {
    return id;
  }

  public BreakPointSpec getBreakPointSpec() {
    return breakPointSpec;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((activityId == null) ? 0 : activityId.hashCode());
    result = prime * result + ((breakPointSpec == null) ? 0 : breakPointSpec.hashCode());
    result = prime * result + ((processDefinitionId == null) ? 0 : processDefinitionId.hashCode());
    result = prime * result + ((transitionId == null) ? 0 : transitionId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BreakPoint other = (BreakPoint) obj;
    if (activityId == null) {
      if (other.activityId != null)
        return false;
    } else if (!activityId.equals(other.activityId))
      return false;
    if (breakPointSpec == null) {
      if (other.breakPointSpec != null)
        return false;
    } else if (!breakPointSpec.equals(other.breakPointSpec))
      return false;
    if (processDefinitionId == null) {
      if (other.processDefinitionId != null)
        return false;
    } else if (!processDefinitionId.equals(other.processDefinitionId))
      return false;
    if (transitionId == null) {
      if (other.transitionId != null)
        return false;
    } else if (!transitionId.equals(other.transitionId))
      return false;
    return true;
  }

  public String toString() {
    return breakPointSpec + " " +
        ( activityId != null ? activityId : "") +
        ( transitionId != null ? transitionId : "")
    ;
  }

}
