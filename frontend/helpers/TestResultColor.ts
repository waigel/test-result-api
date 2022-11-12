import {TestResultType} from "../api/types/TestResultType";

export const TestResultColor = {
    [TestResultType.NEGATIVE]: 'green',
    [TestResultType.POSITIVE]: 'red',
    [TestResultType.INVALID]: 'darkgray',
}