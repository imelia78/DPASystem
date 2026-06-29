import styled, { css } from 'styled-components';

export const Button = styled.button`
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.8rem 1.5rem;
  border-radius: ${({ theme }) => theme.radii.md};
  font-size: 1rem;
  font-weight: 600;
  border: none;
  outline: none;
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};
  width: ${({ fullWidth }) => (fullWidth ? '100%' : 'auto')};

  ${({ variant, theme }) => {
    switch (variant) {
      case 'secondary':
        return css`
          background: ${theme.colors.surface};
          color: ${theme.colors.text};
          border: 1px solid ${theme.colors.border};
          &:hover {
            background: ${theme.colors.surfaceHover};
          }
        `;
      case 'danger':
        return css`
          background: ${theme.colors.danger};
          color: white;
          &:hover {
            background: ${theme.colors.dangerHover};
          }
        `;
      case 'ghost':
        return css`
          background: transparent;
          color: ${theme.colors.primary};
          &:hover {
            background: rgba(99, 102, 241, 0.1);
          }
        `;
      default:
        return css`
          background: ${theme.colors.primary};
          color: white;
          box-shadow: ${theme.shadows.glow};
          &:hover {
            background: ${theme.colors.primaryHover};
            transform: translateY(-2px);
          }
          &:active {
            transform: translateY(0);
          }
        `;
    }
  }}
`;
