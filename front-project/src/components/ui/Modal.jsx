import React from 'react';
import styled, { keyframes } from 'styled-components';
import { X, CheckCircle, AlertCircle, Info } from 'lucide-react';
import { Button } from './Button';

const fadeIn = keyframes`
  from { opacity: 0; transform: scale(0.95) translateY(-10px); }
  to { opacity: 1; transform: scale(1) translateY(0); }
`;

const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const ModalContainer = styled.div`
  background: ${({ theme }) => theme.colors.glass};
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: ${({ theme }) => theme.radii.lg};
  padding: 2rem;
  width: 90%;
  max-width: 400px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  animation: ${fadeIn} 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  position: relative;
`;

const CloseButton = styled.button`
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: transparent;
  border: none;
  color: ${({ theme }) => theme.colors.textMuted};
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.25rem;
  border-radius: ${({ theme }) => theme.radii.sm};

  &:hover {
    color: ${({ theme }) => theme.colors.text};
    background: rgba(255, 255, 255, 0.1);
  }
`;

const IconWrapper = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: ${({ $type, theme }) => 
    $type === 'success' ? 'rgba(16, 185, 129, 0.15)' : 
    $type === 'error' ? 'rgba(239, 68, 68, 0.15)' : 
    'rgba(59, 130, 246, 0.15)'};
  color: ${({ $type, theme }) => 
    $type === 'success' ? theme.colors.success || '#10b981' : 
    $type === 'error' ? theme.colors.danger || '#ef4444' : 
    theme.colors.primary};
  margin: 0 auto 1.25rem;
`;

const Title = styled.h3`
  font-size: 1.25rem;
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  text-align: center;
  margin-bottom: 0.5rem;
`;

const Message = styled.p`
  color: ${({ theme }) => theme.colors.textMuted};
  text-align: center;
  font-size: 0.95rem;
  line-height: 1.5;
  margin-bottom: 1.5rem;
`;

const Footer = styled.div`
  display: flex;
  justify-content: center;
`;

export const Modal = ({ isOpen, onClose, title, message, type = 'info', confirmText = 'OK' }) => {
  if (!isOpen) return null;

  return (
    <Overlay onClick={onClose}>
      <ModalContainer onClick={(e) => e.stopPropagation()}>
        <CloseButton onClick={onClose}>
          <X size={20} />
        </CloseButton>
        
        <IconWrapper $type={type}>
          {type === 'success' && <CheckCircle size={24} />}
          {type === 'error' && <AlertCircle size={24} />}
          {type === 'info' && <Info size={24} />}
        </IconWrapper>

        <Title>{title}</Title>
        <Message>{message}</Message>
        
        <Footer>
          <Button fullWidth onClick={onClose}>
            {confirmText}
          </Button>
        </Footer>
      </ModalContainer>
    </Overlay>
  );
};
