import { useEffect } from 'react';
import styled from 'styled-components';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { User, Users, LogOut, Stethoscope, LayoutDashboard, CalendarDays } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import LanguageSwitcher from '../ui/LanguageSwitcher';
import { extractAvatar } from '../../utils/avatarUtils';

const LayoutContainer = styled.div`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
`;

const Navbar = styled.nav`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 3rem;
  background: ${({ theme }) => theme.colors.surface};
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
  flex-wrap: wrap;
  gap: 1rem;

  @media (max-width: 768px) {
    padding: 1rem;
    flex-direction: column;
  }
`;

const LogoContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.5rem;
  font-weight: 700;
  color: ${({ theme }) => theme.colors.primary};
  cursor: pointer;
`;

const LeftSection = styled.div`
  display: flex;
  align-items: center;
  gap: 3rem;
  flex-wrap: wrap;

  @media (max-width: 768px) {
    gap: 1rem;
    justify-content: center;
  }
`;

const NavLinks = styled.div`
  display: flex;
  align-items: center;
  gap: 2rem;
`;

const NavLink = styled.span`
  color: ${({ active, theme }) => active ? theme.colors.primary : theme.colors.textMuted};
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
  transition: ${({ theme }) => theme.transitions.default};

  &:hover {
    color: ${({ theme }) => theme.colors.text};
  }
`;

const RightSection = styled.div`
  display: flex;
  align-items: center;
  gap: 2rem;
  flex-wrap: wrap;

  @media (max-width: 768px) {
    gap: 1rem;
    justify-content: center;
  }
`;

const UserProfile = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: ${({ theme }) => theme.colors.text};
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
`;

const LogoutButton = styled.button`
  background: transparent;
  color: ${({ theme }) => theme.colors.danger};
  display: flex;
  align-items: center;
  gap: 0.5rem;
  border: none;
  font-weight: 600;
  font-size: 0.95rem;
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};

  &:hover {
    color: ${({ theme }) => theme.colors.dangerHover};
  }
`;

const MainContent = styled.main`
  flex: 1;
  padding: 2rem 3rem;
  max-width: 1400px;
  width: 100%;
  margin: 0 auto;

  @media (max-width: 768px) {
    padding: 1rem;
  }
`;

const DoctorLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { t } = useTranslation();
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/', { replace: true });
  };

  useEffect(() => {
    if (!localStorage.getItem('user')) {
      navigate('/', { replace: true });
    }
  }, [location.pathname, navigate]);

  return (
    <LayoutContainer>
      <Navbar>
        <LeftSection>
          <LogoContainer onClick={() => navigate('/doctor/dashboard')}>
            <Stethoscope size={28} />
            {t('nav.healthBridge')} - {t('nav.doctorPortal')}
          </LogoContainer>
          
          <NavLinks>
            {user.verificationStatus === 'APPROVED' && (
              <>
                <NavLink 
                  active={location.pathname.includes('/dashboard')}
                  onClick={() => navigate('/doctor/dashboard')}
                >
                  <LayoutDashboard size={18} />
                  {t('nav.dashboard')}
                </NavLink>
                <NavLink 
                  active={location.pathname.includes('/schedule')}
                  onClick={() => navigate('/doctor/schedule')}
                >
                  <CalendarDays size={18} />
                  {t('nav.manageSchedule')}
                </NavLink>
              </>
            )}
            <NavLink 
              active={location.pathname.includes('/doctor/browse')}
              onClick={() => navigate('/doctor/browse')}
            >
              <Users size={18} />
              Browse Doctors
            </NavLink>
          </NavLinks>
        </LeftSection>

        <RightSection>
          <LanguageSwitcher />
          <UserProfile onClick={() => navigate('/doctor/profile')}>
            {extractAvatar(user.professionalDescription).photoUrl ? (
              <img 
                src={extractAvatar(user.professionalDescription).photoUrl} 
                alt="Avatar" 
                style={{ width: '24px', height: '24px', borderRadius: '50%', objectFit: 'cover' }} 
              />
            ) : (
              <User size={18} />
            )}
            Dr. {user.lastName || t('nav.profile')}
          </UserProfile>
          <LogoutButton onClick={handleLogout}>
            <LogOut size={18} />
            {t('nav.logout')}
          </LogoutButton>
        </RightSection>
      </Navbar>

      <MainContent>
        <Outlet />
      </MainContent>
    </LayoutContainer>
  );
};

export default DoctorLayout;
