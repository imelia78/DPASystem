import { useEffect } from 'react';
import styled from 'styled-components';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { User, LogOut, HeartPulse, LayoutDashboard, Calendar } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import LanguageSwitcher from '../ui/LanguageSwitcher';

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
`;

const Layout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { t } = useTranslation();
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/', { replace: true });
  };

  // Global authorization check to prevent the "back button" bug
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (!storedUser) {
      navigate('/', { replace: true });
      return;
    }
    
    try {
      const parsedUser = JSON.parse(storedUser);
      if (parsedUser.role === 'doctor') {
        navigate('/doctor/dashboard', { replace: true });
      } else if (parsedUser.role === 'admin') {
        navigate('/admin/dashboard', { replace: true });
      } else if (parsedUser.role !== 'patient') {
        navigate('/', { replace: true });
      }
    } catch (e) {
      navigate('/', { replace: true });
    }
  }, [location.pathname, navigate]);

  return (
    <LayoutContainer>
      <Navbar>
        <LeftSection>
          <LogoContainer onClick={() => navigate('/patient/dashboard')}>
            <HeartPulse size={28} />
            {t('nav.healthBridge')}
          </LogoContainer>
          
          <NavLinks>
            <NavLink 
              active={location.pathname.includes('/dashboard')}
              onClick={() => navigate('/patient/dashboard')}
            >
              <LayoutDashboard size={18} />
              {t('nav.dashboard')}
            </NavLink>
            <NavLink 
              active={location.pathname.includes('/procedures')}
              onClick={() => navigate('/patient/procedures')}
            >
              <Calendar size={18} />
              {t('nav.bookAppointment')}
            </NavLink>
          </NavLinks>
        </LeftSection>

        <RightSection>
          <LanguageSwitcher />
          <UserProfile onClick={() => navigate('/patient/profile')}>
            <User size={18} />
            {user.firstName || t('nav.profile')}
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

export default Layout;
