import React from 'react';
import { useTranslation } from 'react-i18next';
import styled from 'styled-components';
import { Globe } from 'lucide-react';

const SwitcherContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: ${({ theme }) => theme.colors.surfaceHover};
  padding: 0.4rem 0.8rem;
  border-radius: ${({ theme }) => theme.radii.full};
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};

  &:hover {
    background: #e2e8f0;
  }
`;

const Select = styled.select`
  background: transparent;
  border: none;
  font-family: inherit;
  font-size: 0.9rem;
  font-weight: 500;
  color: ${({ theme }) => theme.colors.text};
  cursor: pointer;
  outline: none;
  
  option {
    background: ${({ theme }) => theme.colors.surface};
    color: ${({ theme }) => theme.colors.text};
  }
`;

const LanguageSwitcher = () => {
  const { i18n } = useTranslation();

  const changeLanguage = (lng) => {
    i18n.changeLanguage(lng);
  };

  return (
    <SwitcherContainer>
      <Globe size={18} color="#64748b" />
      <Select 
        value={i18n.language.startsWith('ka') ? 'ka' : 'en'} 
        onChange={(e) => changeLanguage(e.target.value)}
      >
        <option value="en">EN</option>
        <option value="ka">KA</option>
      </Select>
    </SwitcherContainer>
  );
};

export default LanguageSwitcher;
