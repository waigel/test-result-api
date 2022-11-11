import {TestResultType} from "./TestResultType";
import {CWATransmission} from "./CWATransmission";

export interface PublicDecryptedDataResponseDTO {
    testId: string,
    firstName: string,
    lastName: string,
    testPerformedAt: string,
    testName: string,
    testResult: TestResultType,
    cwaTransmission: CWATransmission
}