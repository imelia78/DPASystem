import { useEffect } from 'react';
import styled from 'styled-components';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { ShieldCheck, Users, LogOut, LayoutDashboard } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import LanguageSwitcher from '../ui/LanguageSwitcher';

const AdminLayoutContainer = styled.div`
  display: flex;
  min-height: 100vh;
  background: ${({ theme }) => theme.colors.background};
`;

const Sidebar = styled.aside`
  width: 280px;
  background: #1e293b; /* Slate 800 - dark, professional */
  color: white;
  display: flex;
  flex-direction: column;
`;

const SidebarHeader = styled.div`
  padding: 2rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.5rem;
  font-weight: 700;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  color: ${({ theme }) => theme.colors.primary};
`;

const NavList = styled.nav`
  flex: 1;
  padding: 2rem 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

const NavItem = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 1.5rem;
  border-radius: ${({ theme }) => theme.radii.md};
  cursor: pointer;
  font-weight: 500;
  transition: ${({ theme }) => theme.transitions.default};
  
  background: ${({ $active, theme }) => $active ? `${theme.colors.primary}20` : 'transparent'};
  color: ${({ $active, theme }) => $active ? theme.colors.primary : '#cbd5e1'};
  border-left: 4px solid ${({ $active, theme }) => $active ? theme.colors.primary : 'transparent'};

  &:hover {
    background: ${({ $active, theme }) => $active ? `${theme.colors.primary}20` : 'rgba(255, 255, 255, 0.05)'};
    color: ${({ $active, theme }) => $active ? theme.colors.primary : 'white'};
  }
`;

const SidebarFooter = styled.div`
  padding: 2rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
`;

const LogoutButton = styled.button`
  width: 100%;
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 1.5rem;
  background: transparent;
  border: none;
  color: #f87171;
  font-weight: 600;
  cursor: pointer;
  border-radius: ${({ theme }) => theme.radii.md};
  transition: ${({ theme }) => theme.transitions.default};

  &:hover {
    background: rgba(248, 113, 113, 0.1);
  }
`;

const MainContent = styled.main`
  flex: 1;
  padding: 3rem 4rem;
  overflow-y: auto;
  height: 100vh;
`;

const Topbar = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 3rem;
  
  h1 {
    font-size: 2rem;
    color: ${({ theme }) => theme.colors.text};
  }
  
  .admin-badge {
    background: ${({ theme }) => theme.colors.secondary}15;
    color: ${({ theme }) => theme.colors.secondary};
    padding: 0.5rem 1rem;
    border-radius: ${({ theme }) => theme.radii.full};
    font-weight: 600;
    font-size: 0.9rem;
  }
`;

const AdminLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { t } = useTranslation();

  useEffect(() => {
    // Basic auth check. In reality, check if role is ADMIN
    if (!localStorage.getItem('user')) {
      navigate('/', { replace: true });
    }
  }, [location.pathname, navigate]);

  const handleLogout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    navigate('/', { replace: true });
  };

  return (
    <AdminLayoutContainer>
      <Sidebar>
        <SidebarHeader>
          <ShieldCheck size={28} />
          {t('admin.adminPanel')}
        </SidebarHeader>
        
        <NavList>
          <NavItem 
            $active={location.pathname === '/admin/dashboard'}
            onClick={() => navigate('/admin/dashboard')}
          >
            <LayoutDashboard size={20} />
            {t('nav.dashboard')}
          </NavItem>
          <NavItem 
            $active={location.pathname === '/admin/doctors'}
            onClick={() => navigate('/admin/doctors')}
          >
            <Users size={20} />
            {t('admin.pendingApprovals')}
          </NavItem>
        </NavList>

        <SidebarFooter>
          <LogoutButton onClick={handleLogout}>
            <LogOut size={20} />
            {t('nav.logout')}
          </LogoutButton>
        </SidebarFooter>
      </Sidebar>

      <MainContent>
        <Topbar>
          <h1>
            {location.pathname === '/admin/dashboard' ? t('admin.overview') : t('admin.doctorApprovals')}
          </h1>
          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <LanguageSwitcher />
            <div className="admin-badge">{t('admin.sysAdmin')}</div>
          </div>
        </Topbar>
        <Outlet />
      </MainContent>
    </AdminLayoutContainer>
  );
};

export default AdminLayout;
