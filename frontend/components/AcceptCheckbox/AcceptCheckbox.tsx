import {T} from "@tolgee/react";

export interface AcceptCheckboxProps {
    checked: boolean;
    onChange: (checked: boolean) => void;
}

export const AcceptCheckbox = ({checked, onChange}: AcceptCheckboxProps) => {
    return (<div className="mt-4">
        <div className="flex items-start pt-6">
            <div className="flex items-center h-5">
                <input id="accept" name="accept" type="checkbox" checked={checked}
                       onChange={(e) => onChange(e.target.checked)}
                       required={true}
                       className="focus:ring-brand-500 h-4 w-4 text-brand-600 border-gray-300
                                        rounded"/>
            </div>
            <div className="ml-3 text-sm">
                <label htmlFor="accept" className="font-medium text-gray-700">
                    <T>ACCEPT_TERMS</T>
                </label>
                <p className="text-gray-500">
                    <T>ACCEPT_TERMS_DESCRIPTION</T>
                </p>
            </div>
        </div>
    </div>)
}