import React from "react";

export interface ContainerProps {
    children: React.ReactNode;
}

export const Container = ({children}: ContainerProps) => {
    return (
        <div className={"max-w-7xl mx-auto px-4 sm:px-6 lg:px-8"}>
            <div className={"max-w-3xl mx-auto"}>
                {children}
            </div>
        </div>)
}