import React from 'react';
import { DropdownItem } from 'reactstrap';
import { NavDropdown } from './menu-components';
import { locales, languages } from 'app/config/translation';
import LanguageIcon from '@mui/icons-material/Language';

export const LocaleMenu = ({ currentLocale, onClick }: { currentLocale: string; onClick: (event: any) => void }) =>
  Object.keys(languages).length > 1 ? (
    <NavDropdown icon={<LanguageIcon />} name={currentLocale ? languages[currentLocale].name : undefined}>
      {locales.map(locale => (
        <DropdownItem key={locale} value={locale} onClick={onClick}>
          {languages[locale].name}
        </DropdownItem>
      ))}
    </NavDropdown>
  ) : null;
