import React, { forwardRef, ReactNode } from "react";
import PropTypes from "prop-types";
import MKTypographyRoot from "components/MKTypography/MKTypographyRoot";

interface MKTypographyProps {
  color?: "inherit" | "primary" | "secondary" | "info" | "success" | "warning" | "error" | "light" | "dark" | "text" | "white";
  fontWeight?: false | "light" | "regular" | "medium" | "bold";
  textTransform?: "none" | "capitalize" | "uppercase" | "lowercase";
  verticalAlign?: "unset" | "baseline" | "sub" | "super" | "text-top" | "text-bottom" | "middle" | "top" | "bottom";
  textGradient?: boolean;
  opacity?: number;
  children: ReactNode;
}

const MKTypography = forwardRef<HTMLParagraphElement, MKTypographyProps>(
  (
    { color = "dark", fontWeight = false, textTransform = "none", verticalAlign = "unset", textGradient = false, opacity = 1, children, ...rest },
    ref
  ) => (
    <MKTypographyRoot
      {...rest}
      ref={ref}
      ownerState={{
        color,
        textTransform,
        verticalAlign,
        fontWeight,
        opacity,
        textGradient,
      }}
    >
      {children}
    </MKTypographyRoot>
  )
);

MKTypography.defaultProps = {
  color: "dark",
  fontWeight: false,
  textTransform: "none",
  verticalAlign: "unset",
  textGradient: false,
  opacity: 1,
};

MKTypography.propTypes = {
  color: PropTypes.oneOf([
    "inherit",
    "primary",
    "secondary",
    "info",
    "success",
    "warning",
    "error",
    "light",
    "dark",
    "text",
    "white",
  ]),
  fontWeight: PropTypes.oneOf([false, "light", "regular", "medium", "bold"]),
  textTransform: PropTypes.oneOf(["none", "capitalize", "uppercase", "lowercase"]),
  verticalAlign: PropTypes.oneOf([
    "unset",
    "baseline",
    "sub",
    "super",
    "text-top",
    "text-bottom",
    "middle",
    "top",
    "bottom",
  ]),
  textGradient: PropTypes.bool,
  opacity: PropTypes.number,
  children: PropTypes.node.isRequired,
};

export default MKTypography;
