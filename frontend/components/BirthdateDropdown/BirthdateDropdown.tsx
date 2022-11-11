import {useEffect, useState} from "react";

import Dropdown, {Option} from 'react-dropdown';
import {useTranslate} from "@tolgee/react";

export interface BirthdateDropdownProps {
    onChange: (date: string) => void;
}

export const BirthdateDropdown = ({onChange}: BirthdateDropdownProps) => {
    const t = useTranslate();

    const days: Option[] = Array.from(Array(31), (_, i) => ({value: `${i + 1}`, label: i + 1}));
    const months: Option[] = Array.from(Array(12), (_, i) => ({value: `${i + 1}`, label: t("MONTH_" + (i + 1))}));
    const years: Option[] = Array.from(Array(new Date().getFullYear() + 2 - 1920), (_, i) => ({
        value: `${i + 1920}`,
        label: i + 1920
    })).reverse();

    const [day, setDay] = useState<Option>(days[0]);
    const [month, setMonth] = useState<Option>(months[0]);
    const [year, setYear] = useState<Option>(years[0]);

    useEffect(() => {
        onChange(`${year.value}-${month.value.padStart(2, "0")}-${day.value.padStart(2, "0")}`);
    }, [day, month, year])

    return (
        <div className={"grid grid-cols-3 gap-2"}>
            <Dropdown options={days} onChange={setDay} value={day} placeholder={"Day"}/>
            <Dropdown options={months} onChange={setMonth} value={month} placeholder={"Month"}/>
            <Dropdown options={years} onChange={setYear} value={year} placeholder={"Year"}/>
        </div>
    )
}