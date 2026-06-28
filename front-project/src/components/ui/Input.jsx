import styled from 'styled-components';

export const Input = styled.input`
  width: 100%;
  padding: 0.8rem 1.2rem;
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  color: ${({ theme }) => theme.colors.text};
  font-size: 1rem;
  transition: ${({ theme }) => theme.transitions.default};
  outline: none;

  &:focus {
    border-color: ${({ theme }) => theme.colors.primary};
    box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
  }

  &::placeholder {
    color: ${({ theme }) => theme.colors.textMuted};
  }
`;

export const Select = styled.select`
  width: 100%;
  padding: 0.8rem 1.2rem;
  border-radius: ${({ theme }) => theme.radii.md};
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  color: ${({ theme }) => theme.colors.text};
  font-size: 1rem;
  transition: ${({ theme }) => theme.transitions.default};
  outline: none;
  appearance: none;

  &:focus {
    border-color: ${({ theme }) => theme.colors.primary};
    box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
  }
`;

export const Label = styled.label`
  display: block;
  font-size: 0.9rem;
  color: ${({ theme }) => theme.colors.textMuted};
  margin-bottom: 0.5rem;
`;

export const FormGroup = styled.div`
  margin-bottom: 1.5rem;
  width: 100%;
`;
