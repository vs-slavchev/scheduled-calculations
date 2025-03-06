class CalculationResult {
  constructor(result, startedAt, finishedAt, scheduleType, scheduleString) {
    this.result = result;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
    this.scheduleType = scheduleType;
    this.scheduleString = scheduleString;
  }
}

export default CalculationResult;